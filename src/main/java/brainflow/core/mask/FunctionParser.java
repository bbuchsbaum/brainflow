package brainflow.core.mask;

import org.codehaus.jparsec.*;
import org.codehaus.jparsec.functors.Map;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 30, 2008
 * Time: 12:50:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunctionParser {


    private static final Parser<Void> IGNORED = Scanners.WHITESPACES.skipMany();

    private static final Terminals OPERATORS = Terminals.operators("+", "-", "*", "/", "(", ")", "or", "and", "not", ">", "<");

    private static final Terminals COMMA = Terminals.operators(",");

   // private static final Terminals FUNCNAMES = Terminals.caseInsensitive(new String[]{}, new String[] {"thresh"});




    final static Parser<?> TOKENIZER = Parsers.or(Terminals.DecimalLiteral.TOKENIZER,
            (Parser<? extends Tokens.Fragment>) OPERATORS.tokenizer(),
            Terminals.Identifier.TOKENIZER,
            (Parser<? extends Tokens.Fragment>) COMMA.tokenizer());
            //(Parser<? extends Tokens.Fragment>) FUNCNAMES.tokenizer());

    private Parser<?> getParser() {

        final Parser<Double> NUMBER = Terminals.DecimalLiteral.PARSER.map(new Map<String, Double>() {
            public Double map(String s) {
                return Double.valueOf(s);
            }
        });

        final Parser<String> VARIABLE = Terminals.Identifier.PARSER.map(new Map<String, String>() {
            public String map(String s) {
                return s;
            }
        });

        Parser<?> VALUE = Parsers.or(NUMBER, VARIABLE);

        Parser<? extends List<?>> ARGLIST = VALUE.sepBy(COMMA.token(","));

        Parser<? extends List<?>> ARGPARSER = Parsers.between(OPERATORS.token("("), ARGLIST,
                OPERATORS.token(")"));

        Parser<?> FUNC_PARSER = Parsers.pair(VARIABLE, ARGPARSER);
        return FUNC_PARSER;

    }


    public static void main(String[] args) {
        FunctionParser fp = new FunctionParser();

        Parser<?> parser = fp.getParser();
        //parser.from(TOKENIZER, IGNORED)
        Object obj = parser.from(TOKENIZER, IGNORED).parse("uthr(1,2, 5)");

        System.out.println(obj);
    }


}
