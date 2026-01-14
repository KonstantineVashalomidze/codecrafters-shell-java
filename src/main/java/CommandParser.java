import java.util.ArrayList;
import java.util.List;

public class CommandParser {

    private enum State {
        NORMAL,
        IN_QUOTE_SINGLE,
        IN_QUOTE_DOUBLE,
        BACKSLASH
    }

    public ParsedInput parse(String rawInput) {

        final List<String> tokens = new ArrayList<>();

        StringBuilder currentToken = new StringBuilder();

        State currentState = State.NORMAL;
        State previousState = State.NORMAL;

        boolean tokenInProgress = false;

        for (int i = 0; i < rawInput.length(); i++) {
            char c = rawInput.charAt(i);

            switch (currentState) {
                case NORMAL -> {
                    if (c == '\'') {
                        currentState = State.IN_QUOTE_SINGLE;
                        tokenInProgress = true;
                    } else if (c == '"') {
                        currentState = State.IN_QUOTE_DOUBLE;
                        tokenInProgress = true;
                    } else if (Character.isWhitespace(c)) {
                        if (tokenInProgress) {
                            tokens.add(currentToken.toString());
                            currentToken.setLength(0);
                            tokenInProgress = false;
                        }
                    } else if (c == '\\') {
                        currentState = State.BACKSLASH;
                        tokenInProgress = true;
                    }
                    else {
                        currentToken.append(c);
                        tokenInProgress = true;
                    }
                }
                case IN_QUOTE_SINGLE -> {
                    if (c == '\'') {
                        currentState = State.NORMAL;
                    } else if (c == '\\') {
                        currentState = State.BACKSLASH;
                        previousState = State.IN_QUOTE_SINGLE;

                    } else {
                        currentToken.append(c);
                    }
                }
                case IN_QUOTE_DOUBLE -> {
                    if (c == '"') {
                        currentState = State.NORMAL;
                    } else if (c == '\\') {
                        currentState = State.BACKSLASH;
                        previousState = State.IN_QUOTE_DOUBLE;

                    } else {
                        currentToken.append(c);
                    }
                }
                case BACKSLASH -> {
                    currentState = previousState;
                    currentToken.append(c);
                }
                default -> {
                    // Ignored
                }
            }

        }

        if (tokenInProgress) {
            tokens.add(currentToken.toString());
        }


        return new ParsedInput(tokens.getFirst(), tokens.subList(1, tokens.size()));

    }



}