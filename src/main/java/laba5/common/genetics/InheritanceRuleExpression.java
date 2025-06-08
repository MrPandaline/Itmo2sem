package laba5.common.genetics;

public class InheritanceRuleExpression implements Expression {
    private final String rule;

    public InheritanceRuleExpression(String rule) {
        this.rule = rule.toLowerCase();
    }

    @Override
    public char interpret(Context context) {
        if (rule.contains("dominant")) {
            return context.parent1().getDominantAllele();
        } else if (rule.contains("recessive")) {
            return context.parent2().isRecessive() ? context.parent2().getDominantAllele() : 'x';
        } else {
            return Math.random() < 0.5 ? context.parent1().allele1 : context.parent2().allele1;
        }
    }
}