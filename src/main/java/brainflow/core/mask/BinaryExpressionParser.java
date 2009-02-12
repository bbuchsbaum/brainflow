package brainflow.core.mask;


import brainflow.image.operations.BinaryOperand;
import brainflow.image.operations.UnaryOperand;
import org.codehaus.jparsec.*;
import org.codehaus.jparsec.functors.Binary;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Unary;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 1:51:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryExpressionParser {

    private static final Terminals OPERATORS = Terminals.operators("+", "-", "*", "/", "(", ")", "or", "and", "not", ">", "<");

    private static final Parser<Void> IGNORED = Scanners.WHITESPACES.skipMany();

    //static final Parser<?> TOKENIZER = Terminals.DecimalLiteral.TOKENIZER.or(OPERATORS.tokenizer());

    enum UnaryOperator implements Unary<INode> {
        NEG {
            public INode map(INode iNode) {
                return new UnaryNode(iNode, UnaryOperand.NEGATE);
                
            }
        }
    }

    enum BinaryOperator implements Binary<INode> {
        AND {
            public INode map(INode a, INode b) {
                return new ComparisonNode(a, b, BinaryOperand.AND);
            }
        },

        EQ {
            public INode map(INode a, INode b) {
                return new ComparisonNode(a, b, BinaryOperand.EQ);
            }
        },

        OR {
            public INode map(INode a, INode b) {
                return new ComparisonNode(a, b, BinaryOperand.OR);
            }
        },

        GT {
            public INode map(INode a, INode b) {
                return new ComparisonNode(a, b, BinaryOperand.GT);
            }
        },

        LT {
            public INode map(INode a, INode b) {
                return new ComparisonNode(a, b, BinaryOperand.LT);
            }
        }


    }


    private static Parser<?> term(String... names) {
        return OPERATORS.token(names);
    }

    static <T> Parser<T> op(String name, T value) {
        return term(name).retn(value);
    }

    private Context context;

    public BinaryExpressionParser() {
        this.context = new Context() {
            public Object getValue(String symbol) {
                return "xxx";
            }
        };
    }

    public BinaryExpressionParser(Context context) {
        this.context = context;
    }

    private Parser<ValueNode> constantParser() {
        return Terminals.DecimalLiteral.PARSER.map(new Map<String, ValueNode>() {
            public ValueNode map(String s) {
                return new ConstantNode(Double.valueOf(s));
            }
        });

    }

    private Parser<ValueNode> variableParser() {
        return Terminals.Identifier.PARSER.map(new Map<String, ValueNode>() {
            public ValueNode map(String s) {
                return new VariableNode(s, context);
            }
        });

    }


    public Parser<INode> createParser() {


        final Parser<?> TOKENIZER = Parsers.or(Terminals.DecimalLiteral.TOKENIZER,
                (Parser<? extends Tokens.Fragment>) OPERATORS.tokenizer(),
                Terminals.Identifier.TOKENIZER);


        final Parser<ValueNode> NUMBER = constantParser();

        final Parser<ValueNode> VARIABLE = variableParser();


        Parser.Reference<INode> ref = Parser.newReference();
        Parser<INode> unit = ref.lazy().between(term("("), term(")")).or(NUMBER).or(VARIABLE);
        Parser<INode> optable = new OperatorTable<INode>()

                .infixl(op("and", BinaryOperator.AND), 10)
                .infixl(op("or", BinaryOperator.OR), 10)
                .infixl(op("<", BinaryOperator.LT), 20)
                .infixl(op(">", BinaryOperator.GT), 20)
                .prefix(op("-", UnaryOperator.NEG), 30)

                .build(unit);

        ref.set(optable);


        return optable.from(TOKENIZER, IGNORED);


        /*final Parser<_> s_whitespace = Scanners.isWhitespaces();
        final Parser<Tok> l_number = Lexers.decimal("number");
        final Parser<Tok> l_variable = Lexers.word("variable");


        final Terms ops = Terms.getOperatorsInstance("or", "and", "not", "-", "<", ">", "(", ")");

        final Parser<Tok> l_tok = Parsers.plus(ops.getLexer(), l_number, l_variable);
        final Parser<Tok[]> lexer = Lexers.lexeme(s_whitespace.many(), l_tok).followedBy(Parsers.eof());

        final Parser<INode> p_constant = Terms.decimalParser(new FromString<INode>() {
            public INode fromString(int from, int len, String s) {
                return new ConstantNode(Double.valueOf(s));
            }
        });

        final Parser<INode> p_variable = Terms.wordParser(new FromString<INode>() {
            public INode fromString(int from, int len, String s) {
                return new VariableNode(s);
            }
        });

        final Parser<Tok> or_op = ops.getParser("or");

        final Parser<Tok> and_op = ops.getParser("and");
        final Parser<Tok> not_op = ops.getParser("not");
        final Parser<Tok> gt_op = ops.getParser(">");
        final Parser<Tok> lt_op = ops.getParser("<");
        final Parser<Tok> neg_op = ops.getParser("-");
        final Parser<Tok> p_lparen = ops.getParser("(");
        final Parser<Tok> p_rparen = ops.getParser(")");

        final Parser<Binary<INode>> p_lt = lt_op.seq(Parsers.retn(toMap2(BinaryOperand.LT)));
        final Parser<Binary<INode>> p_gt = gt_op.seq(Parsers.retn(toMap2(BinaryOperand.GT)));
        final Parser<Binary<INode>> p_and = and_op.seq(Parsers.retn(toMap2(BinaryOperand.AND)));
        final Parser<Binary<INode>> p_or = or_op.seq(Parsers.retn(toMap2(BinaryOperand.OR)));


        //lt_op.seq(Parsers.retn())


        final Parser<INode>[] expr_holder = new Parser[1];
        final Parser<INode> p_lazy_expr = Parsers.lazy(expr_holder);
        final Parser<INode> p_term = Parsers.plus(
                Parsers.between(p_lparen, p_rparen, p_lazy_expr),
                p_constant,
                p_variable
        );

        final OperatorTable<INode> optable = new OperatorTable<INode>()
                .infixl(p_lt, 10)
                .infixl(p_gt, 10)
                .infixl(p_and, 20)
                .infixl(p_or, 20);


        final Parser<INode> p_expr = Expressions.buildExpressionParser(p_term, optable);
        expr_holder[0] = p_expr;
        return Parsers.parseTokens(lexer, p_expr.followedBy(Parsers.eof()), "mask calculator");

        */


    }


    public static void main(String[] args) {

        Parser<INode> parser = new BinaryExpressionParser().createParser();
        INode node = parser.parse("-5 > 6");

        MaskEvaluator ms = new MaskEvaluator();
        ms.start(new RootNode(node));
        System.out.println("node : " + node);
        //VariableSubstitution builder = new VariableSubstitution(null);
        //builder.start(node);


    }
}
