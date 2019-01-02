package be.butskri.test.springboot.applicationcontext.autoconfiguretest;

class RegexBuilder {

    private static RegexBuilder ANYTHING = new RegexBuilder("(?s:.*)");
    private final String regex;

    static String regex(RegexBuilder regexBuilder) {
        return regexBuilder.regex;
    }

    static RegexBuilder regex(String regex) {
        return new RegexBuilder(regex);
    }

    static RegexBuilder anything() {
        return ANYTHING;
    }

    private RegexBuilder(String regex) {
        this.regex = regex;
    }

    RegexBuilder andThen(String regex) {
        return new RegexBuilder(this.regex + regex);
    }

    RegexBuilder andThen(RegexBuilder regex) {
        return andThen(regex.regex);
    }

}
