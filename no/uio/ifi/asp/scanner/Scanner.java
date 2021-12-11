package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;


    public Scanner(String fileName) {
	curFileName = fileName;
	indents.push(0);

	try {
	    sourceFile = new LineNumberReader(
			    new InputStreamReader(
				new FileInputStream(fileName),
				"UTF-8"));
	} catch (IOException e) {
	    scannerError("Cannot read " + fileName + "!");
	}
    }


    private void scannerError(String message) {
	String m = "Asp scanner error";
	if (curLineNum() > 0)
	    m += " on line " + curLineNum();
	m += ": " + message;

	Main.error(m);
    }


    public Token curToken() {
	while (curLineTokens.isEmpty()) {
	    readNextLine();
	}
	return curLineTokens.get(0);
    }


    public void readNextToken() {
	if (! curLineTokens.isEmpty())
	    curLineTokens.remove(0);
    }


    private void readNextLine() {
	curLineTokens.clear();

	// Read the next line:
	String line = null;
	try {
	    line = sourceFile.readLine();
	    if (line == null) {
		sourceFile.close();
		sourceFile = null;
	    } else {
		Main.log.noteSourceLine(curLineNum(), line);
	    }
	} catch (IOException e) {
	    sourceFile = null;
	    scannerError("Unspecified I/O error!");
	}
	
	//-- Must be changed in part 1:
	boolean commentLine = false; // check if the line is a comment line
	boolean onlySpaces = false; // check if the line contains only spaces
	boolean lastLine = false; // check if the line read is the last line

	String trimLine = line.trim();
	char[] arr = trimLine.toCharArray();

	// the line contains only spaces if char array produced from it is empty
	// if array is not empty but the line devoid of leading spaces starts with # the line is a comment line
	if (arr.length == 0) {
		onlySpaces = true;
	}
	else if (trimLine.charAt(0) == '#') {
		commentLine = true;
	}

	// if buffer stream is not ready to be read that means that the current line is the last line
	try {
		if (!sourceFile.ready()) {
			lastLine = true;
		}
	}
	catch (IOException e) {
		e.printStackTrace();
	}

	// if current line is neither commentline or contains only spaces start tokenization
	if (!commentLine && !onlySpaces) {
		line = expandLeadingTabs(line); // replces all tabs with spaces
		int n = findIndent(line); // find indent for current line

		// if indent of the current line is larger than that of the previous one
		// larger indent gets pushed to indents stack and we add an indent token to
		// curLineTokens list
		if (n > indents.peek()) {
			indents.push(n);
			curLineTokens.add(new Token(indentToken, curLineNum()));
		}
		// if current indent is less than that of a previous lines we pop the indent from indents
		// stack and add a dedent token to curLineTokens list
		else if (n < indents.peek()) {
			while (n < indents.peek()) {
				indents.pop();
				curLineTokens.add(new Token(dedentToken, curLineNum()));
			}
			// now when topmost indent is not equal to current indent there is a
			// indentation error
			if (n != indents.peek()) {
				Main.error(String.format("Asp scanner error on line %d: Indentation error!", curLineNum()));
			}
		}

		// starting tokenization itself
		for (int i = 0; i < line.length(); i++) {
			char curChar = line.charAt(i); // current character
			// whenever while parsing a line if a program encounters a # character
			// it ignores everything that is beyound that symbol
			if (curChar == '#') {
				break;
			}
			// if current character is a space tab or newline character do nothing go to the next character
			else if (Character.isWhitespace(curChar)) {
				continue;
			}
			// if current character is either a letter or underscore we found either a name or
			// keyword token
			else if (isLetterAZ(curChar) || curChar == '_') {
				StringBuilder sb = new StringBuilder();
				sb.append(curChar);

				// while line is not fully parsed fetch the next character and if this next character
				// is either letter number or underscore append it to StringBuilder because it
				// belongs to current token when that is not the case eg line has ended or fetched character
				// is not a letter underscore or a number we check if a token is a keyword
				while (true) {
					if (i + 1 != line.length()) {
						if (isLetterAZ(line.charAt(i + 1))
								|| isDigit(line.charAt(i + 1))
								|| line.charAt(i + 1) == '_') {
							sb.append(line.charAt(i + 1));
							i++;
						}
						else {
							Token token = new Token(nameToken, curLineNum());
							token.name = sb.toString();
							token.checkResWords();
							curLineTokens.add(token);
							break;
						}
					}
					else {
						Token token = new Token(nameToken, curLineNum());
						token.name = sb.toString();
						token.checkResWords();
						curLineTokens.add(token);
						break;
					}
				}
			}
			// if current character is a digit begin fetching next characters
			// if fetched character is either a digit or a decimal separator
			// append to a current StringBuilder
			// when the next character is no longer a digit or decimal separator
			// use addNumber method to determine if a number is an integer or a float
			// and whether number is valid
			else if (isDigit(curChar)) {
				StringBuilder sb = new StringBuilder();
				sb.append(curChar);

				while (true) {
					if (i + 1 != line.length()) {
						// if current character is a zero not followed by decimal separator add
						// it to curLineTokens as a zero integer token
						if (curChar == '0' && line.charAt(i + 1) != '.' && sb.length() == 1) {
							Token token = new Token(integerToken, curLineNum());
							token.integerLit = 0;
							curLineTokens.add(token);
							break;
						}
						else if (isDigit(line.charAt(i + 1)) || line.charAt(i + 1) == '.') {
							sb.append(line.charAt(i + 1));
							i++;
						}
						// if the next fetched character is a letter that is not a valid number literal
						else if (isLetterAZ(line.charAt(i + 1))) {
							sb.append(line.charAt(i + 1));
							Main.error(String.format("Asp scanner error on line %d: Illegal number literal %s!", curLineNum(), sb.toString()));
							break;
						}
						else {
							addNumber(sb);
							break;
						}
					}
					else {
						addNumber(sb);
						break;
					}
				}
			}
			// if current character has unicodes 34 or 39 we found a string literal
			// start to fetch character that belongs to current string
			else if (curChar == '"' || curChar == '\'') {
				char stringStart = curChar; // character that opened the string
				StringBuilder sb = new StringBuilder();

				while (true) {
					// if next character is not the end of line
					if (i + 1 != line.length()) {
						// if next character is not string start character append current character
						if (line.charAt(i + 1) != stringStart) {
							sb.append(line.charAt(i + 1));
							i++;
						}
						// else the string has ended add current stringliteral to curLineTokens
						else {
							Token token = new Token(stringToken, curLineNum());
							token.stringLit = sb.toString();
							curLineTokens.add(token);
							i++;
							break;
						}
					}
					// if next character is end of line and string is not terminated cast an Exception
					else {
						Main.error(String.format("Asp scanner error on line %d: String literal not terminated!", curLineNum()));
						break;
					}
				}
			}
			// if current character is not anything from above
			else {
				// check if the current character is one of compound operator dellimeter eg either = or ==
				switch (curChar) {
					case '=':
						if (i + 1 != line.length()) {
							if (line.charAt(i + 1) == '=') {
								curLineTokens.add(new Token(doubleEqualToken, curLineNum()));
								i++;
							}
							else {
								curLineTokens.add(new Token(equalToken, curLineNum()));
							}
						}
						else {
							curLineTokens.add(new Token(equalToken, curLineNum()));
						}
						break;
					case '/':
						if (i + 1 != line.length()) {
							if (line.charAt(i + 1) == '/') {
								curLineTokens.add(new Token(doubleSlashToken, curLineNum()));
								i++;
							}
							else {
								curLineTokens.add(new Token(slashToken, curLineNum()));
							}
						}
						else {
							curLineTokens.add(new Token(slashToken, curLineNum()));
						}
						break;
					case '>':
						if (i + 1 != line.length()) {
							if (line.charAt(i + 1) == '=') {
								curLineTokens.add(new Token(greaterEqualToken, curLineNum()));
								i++;
							}
							else {
								curLineTokens.add(new Token(greaterToken, curLineNum()));
							}
						}
						else {
							curLineTokens.add(new Token(greaterToken, curLineNum()));
						}
						break;
					case '<':
						if (i + 1 != line.length()) {
							if (line.charAt(i + 1) == '=') {
								curLineTokens.add(new Token(lessEqualToken, curLineNum()));
								i++;
							}
							else {
								curLineTokens.add(new Token(lessToken, curLineNum()));
							}
						}
						else {
							curLineTokens.add(new Token(lessToken, curLineNum()));
						}
						break;
					case '!':
						if (i + 1 != line.length()) {
							if (line.charAt(i + 1) == '=') {
								curLineTokens.add(new Token(notEqualToken, curLineNum()));
								i++;
							}
							else {
								Main.error(String.format("Asp scanner error on line %d: Illegal character: '!'!", curLineNum()));
							}
						}
						else {
							Main.error(String.format("Asp scanner error on line %d: Illegal character: '!'!", curLineNum()));
						}
						break;
					default:
						String tok = String.valueOf(curChar);
						boolean legalCharacter = false;

						// check if the current character is either a delimiter of an operator
						for (TokenKind tk: EnumSet.range(astToken, semicolonToken)) {
							if (tok.equals(tk.image)) {
								curLineTokens.add(new Token(tk, curLineNum()));
								legalCharacter = true;
								break;
							}
						}
						// if not cast an Exception
						if (!legalCharacter) {
							Main.error(String.format("Asp scanner error on line %d: Illegal character: '%c'!", curLineNum(), curChar));
						}
				}
			}
		}
		// after the line was fully parsed add newLineToken
		curLineTokens.add(new Token(newLineToken, curLineNum()));
	}



	// if current line is the last line add a dedent token for every integer i indents while the topmost indent is
	// is not equal to 0 then add end of file token
	if (lastLine) {
		while (indents.pop() != 0) {
			curLineTokens.add(new Token(dedentToken));
		}
		curLineTokens.add(new Token(eofToken));
	}

	for (Token t: curLineTokens) {
		Main.log.noteToken(t);
	}
    }


	private void addNumber(StringBuilder sb) {
    	/* Method takes in a stringbuilder that was produced while building a string that represents a number
    	* literal and determines whether the number is a valid number and what particular type the number is eg
    	* float or integer when both validity and type has been determined the method produces corresponding
    	* token to curLineTokens list*/
		String number = sb.toString();
		int counter = 0; // decimal separator counter

		for (char c : number.toCharArray()) { // count number of decimal separators
			if (c == '.') {
				counter++;
			}
		}

		if (number.contains(".")) { // check whether the number is a float
			// if there are more than 1 decimal separator in a number
			// number is not legal
			if (counter > 1) {
				Main.error(String.format("Asp scanner error on line %d: Illegal float literal: %s!", curLineNum(), number));
			}
			// 0. and 0.. are not legal floats
			else if (number.charAt(0) == '0') {
				if (number.length() < 3) {
					Main.error(String.format("Asp scanner error on line %d: Illegal float literal: %s!", curLineNum(), number));
				}
				else {
					if (number.charAt(1) == '.' && isDigit(number.charAt(2))) {
						Token token = new Token(floatToken, curLineNum());
						token.floatLit = Double.parseDouble(number);
						curLineTokens.add(token);
					}
					else {
						Main.error(String.format("Asp scanner error on line %d: Illegal float literal: %s!", curLineNum(), number));
					}
				}
			}
			// a float cannot end with a decimal separator
			else {
				if (number.charAt(number.length() - 1) == '.') {
					Main.error(String.format("Asp scanner error on line %d: Illegal float literal: %s!", curLineNum(), number));
				}
				else {
					Token token = new Token(floatToken, curLineNum());
					token.floatLit = Double.parseDouble(number);
					curLineTokens.add(token);
				}
			}
		}
		// number that contains only integers not starting with 0 is a valid integer
		else {
			Token token = new Token(integerToken, curLineNum());
			token.integerLit = Integer.parseInt(number);
			curLineTokens.add(token);
		}
	}

	public int curLineNum() {
	return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
	int indent = 0;

	while (indent<s.length() && s.charAt(indent)==' ') indent++;
	return indent;
    }

    private String expandLeadingTabs(String s) {
	String newS = "";
	for (int i = 0;  i < s.length();  i++) {
	    char c = s.charAt(i);
	    if (c == '\t') {
		do {
		    newS += " ";
		} while (newS.length()%TABDIST > 0);
	    } else if (c == ' ') {
		newS += " ";
	    } else {
		newS += s.substring(i);
		break;
	    }
	}
	return newS;
    }


    private boolean isLetterAZ(char c) {
	return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }


    public boolean isCompOpr() {
		TokenKind k = curToken().kind;
		TokenKind arr[] = new TokenKind[]{lessToken, greaterToken, doubleEqualToken, greaterEqualToken, lessEqualToken
		, notEqualToken};
		List<TokenKind> kinds =  Arrays.asList(arr);
		return kinds.contains(k);
	}


    public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		if (k == plusToken || k == minusToken) {
			return true;
		}
		return false;
    }


    public boolean isFactorOpr() {
		TokenKind k = curToken().kind;
		if (k == astToken
				|| k == slashToken
				|| k == percentToken
				|| k == doubleSlashToken) {
			return true;
		}
	return false;
    }
	

    public boolean isTermOpr() {
		TokenKind k = curToken().kind;
		if (k == plusToken || k == minusToken) {
			return true;
		}
		return false;
    }


    public boolean anyEqualToken() {
	for (Token t: curLineTokens) {
	    if (t.kind == equalToken) return true;
	    if (t.kind == semicolonToken) return false;
	}
	return false;
    }
}
