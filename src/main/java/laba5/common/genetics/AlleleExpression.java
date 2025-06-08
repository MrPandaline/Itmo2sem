package laba5.common.genetics;

public class AlleleExpression implements Expression {
    private final char allele;

    public AlleleExpression(char allele) {
        this.allele = allele;
    }

    @Override
    public char interpret(Context context) {
        return allele;
    }
}
