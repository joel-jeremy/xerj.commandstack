/**
 * Copyright 2021 Joel Jeremy Marquez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.xerprojects.xerj.commandstack.internal.utils;

import java.util.function.Predicate;

/**
 * Method argument utilities.
 * 
 * @author Joel Jeremy Marquez
 */
public class Arguments {
    private Arguments() {}

    /**
     * Check if argument if null. If so, throw an {@link IllegalArgumentException}.
     * @param <T> Argument type.
     * @param arg The argument to check. 
     * @param argName The name of the argument variable.
     * @return The non-null argument.
     */
    public static <T> T requireNonNull(T arg, String argName) {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " argument must not be null.");
        }

        return arg;
    }

    /**
     * Check if argument if null. If so, throw an {@link IllegalArgumentException}.
     * @param <T> Argument type.
     * @param arg The argument to check.
     * @param throwCondition The condition to throw an {@link IllegalArgumentException}. 
     * @param message The message to put in the {@link IllegalArgumentException}.
     * @return The non-null argument.
     */
    public static <T> T require(T arg, Predicate<T> throwCondition, String message) {
        if (throwCondition.test(arg)) {
            throw new IllegalArgumentException(message);
        }

        return arg;
    }

    /**
     * String argument utilities.
     */
    public static class Strings {

        private Strings() {}

        /**
         * Check if string is null or empty. If so, throw an {@link IllegalArgumentException}.
         * @param arg The string argument to check.
         * @param argName The name of the string variable.
         * @return The non-null/non-empty argument.
         */
        public static String requireNonNullOrEmpty(String arg, String argName) {
            if (arg == null || arg.isEmpty()) {
                throw new IllegalArgumentException(argName + " argument must not be null or empty.");
            }
            return arg;
        }
    }
}
