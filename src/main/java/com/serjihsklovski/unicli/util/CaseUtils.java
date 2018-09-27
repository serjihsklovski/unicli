package com.serjihsklovski.unicli.util;

/**
 * Case converter functions.
 */
public final class CaseUtils {

    /**
     * Returns CamelCase string converted into lowercase hyphened.
     *
     * Examples:
     *
     * "CamelCase" -> "camel-case"
     * "camelCase" -> "camel-case"
     * "dX" -> "d-x"
     * "HTML" -> "html"
     */
    public static String convertCamelCaseToHyphenedLowercase(String camelCase) {
        return convertCamelCaseToHyphened(camelCase).toLowerCase();
    }

    private static String convertCamelCaseToHyphened(String camelCase) {
        StringBuilder resultBuilder = new StringBuilder();
        Character prev;
        char curr;
        Character next;
        for (int i = 0; i < camelCase.length(); i++) {
            prev = (i > 0) ? camelCase.charAt(i - 1) : null;
            curr = camelCase.charAt(i);
            next = ((i + 1) < camelCase.length()) ? camelCase.charAt(i + 1) : null;

            if (
                    Character.isUpperCase(curr) &&
                    (prev != null) && (
                            Character.isLowerCase(prev) || (
                                    ((next != null) || Character.isLowerCase(prev)) &&
                                    ((next == null) || Character.isLowerCase(next))
                            )
                    )
            ) {
                resultBuilder.append('-');
            }
            resultBuilder.append(curr);
        }
        return resultBuilder.toString();
    }

}
