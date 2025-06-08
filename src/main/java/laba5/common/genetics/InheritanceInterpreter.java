package laba5.common.genetics;

import laba5.common.genetics.*;

import java.util.ArrayList;
import java.util.List;

public class InheritanceInterpreter {
    private final List<Expression> expressions = new ArrayList<>();

    public void addRule(String rule) {
        if (rule.contains("allele")) {
            expressions.add(new AlleleExpression(rule.charAt(0)));
        } else {
            expressions.add(new InheritanceRuleExpression(rule));
        }
    }

    public char evaluate(Gene gene1, Gene gene2) {
        Context context = new Context(gene1, gene2);
        for (Expression expr : expressions) {
            return expr.interpret(context);
        }
        return 'x';
    }
}