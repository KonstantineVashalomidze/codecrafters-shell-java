import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandParser {

    private enum State {
        UNQUOTED,
        SINGLE_QUOTED,
        DOUBLE_QUOTED,
        BACKSLASH
    }

    public ParsedInput parse(String rawInput) {

        final List<String> tokens = new ArrayList<>();

        StringBuilder currentToken = new StringBuilder();

        State currentState = State.UNQUOTED;
        State previousState = State.UNQUOTED;
        Set<Character> specialCharacters = Set.of('$', '"', '\\', '*');

        boolean tokenInProgress = false;

        for (int i = 0; i < rawInput.length(); i++) {
            char c = rawInput.charAt(i);

            switch (currentState) {
                case UNQUOTED -> {
                    if (c == '\'') {
                        currentState = State.SINGLE_QUOTED;
                        tokenInProgress = true;
                    } else if (c == '"') {
                        currentState = State.DOUBLE_QUOTED;
                        tokenInProgress = true;
                    } else if (Character.isWhitespace(c)) {
                        if (tokenInProgress) {
                            tokens.add(currentToken.toString());
                            currentToken.setLength(0);
                            tokenInProgress = false;
                        }
                    } else if (c == '\\') {
                        currentState = State.BACKSLASH;
                        previousState = State.UNQUOTED;
                        tokenInProgress = true;
                    }
                    else {
                        currentToken.append(c);
                        tokenInProgress = true;
                    }
                }
                case SINGLE_QUOTED -> {
                    if (c == '\'') {
                        currentState = State.UNQUOTED;
                    } else if (c == '\\') {
                        currentState = State.BACKSLASH;
                        previousState = State.SINGLE_QUOTED;
                    } else {
                        currentToken.append(c);
                    }
                }
                case DOUBLE_QUOTED -> {
                    if (c == '"') {
                        currentState = State.UNQUOTED;
                    } else if (c == '\\') {
                        currentState = State.BACKSLASH;
                        previousState = State.DOUBLE_QUOTED;
                    } else {
                        currentToken.append(c);
                    }
                }
                case BACKSLASH -> {
                    if (previousState == State.SINGLE_QUOTED) {
                        currentToken.append('\\');
                    }  else if (previousState == State.DOUBLE_QUOTED) {
                        if (!specialCharacters.contains(c)) {
                            currentToken.append('\\');
                        }
                    }
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



        String outputFile = null;
        int redirectIndex = -1;

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals(">")) {
                redirectIndex = i;
                if (i + 1 < tokens.size()) {
                    outputFile = tokens.get(i + 1);
                }
                break;
            }
        }

        if (redirectIndex != -1) {
            if (redirectIndex + 1 < tokens.size()) {
                tokens.remove(redirectIndex + 1);
            }
            tokens.remove(redirectIndex);
        }

        return new ParsedInput(tokens.getFirst(), tokens.subList(1, tokens.size()), outputFile);

    }



}