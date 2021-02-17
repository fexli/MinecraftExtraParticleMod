package com.fexli.particle.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;

public class ExpressionArgumentType implements ArgumentType<String> {
    private final ExpressionArgumentType.StringType type;

    private ExpressionArgumentType(final ExpressionArgumentType.StringType type) {
        this.type = type;
    }

    public static ExpressionArgumentType expression() {
        return new ExpressionArgumentType(ExpressionArgumentType.StringType.EXPR);
    }

    public static String getString(final CommandContext<?> context, final String name) {
        return context.getArgument(name, String.class);
    }

    public ExpressionArgumentType.StringType getType() {
        return type;
    }

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {

//        if (type == ExpressionArgumentType.StringType.GREEDY_PHRASE) {
//            final String text = reader.getRemaining();
//            reader.setCursor(reader.getTotalLength());
//            return text;
//        } else
        if (type == StringType.EXPR) {
            int start = reader.getCursor();
            int i = start;
            if (!reader.canRead()) return "";
            while (reader.canRead() && reader.peek() != ' '){
                i++;
                reader.setCursor(i);
            }
            return reader.getString().substring(start,i-1);
        } else {
            return reader.readString();
        }
    }

    @Override
    public String toString() {
        return "string()";
    }

    @Override
    public Collection<String> getExamples() {
        return type.getExamples();
    }

    public static String escapeIfRequired(final String input) {
        for (final char c : input.toCharArray()) {
            if (!StringReader.isAllowedInUnquotedString(c)) {
                return escape(input);
            }
        }
        return input;
    }

    private static String escape(final String input) {
        final StringBuilder result = new StringBuilder("\"");

        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c == '\\' || c == '"') {
                result.append('\\');
            }
            result.append(c);
        }

        result.append("\"");
        return result.toString();
    }

    public enum StringType {
        EXPR("word", "words;with");

        private final Collection<String> examples;

        StringType(final String... examples) {
            this.examples = Arrays.asList(examples);
        }

        public Collection<String> getExamples() {
            return examples;
        }
    }
}
