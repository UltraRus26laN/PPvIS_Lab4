package sample;

public class Model
{
    private String formula=new String();
    public Model(){ this.formula=""; }
    public String getFormula(){ return formula; }
    public void addSymbol(String symbolToAdd){ formula+=symbolToAdd; }
    public void clearFormula(){ formula=""; }
    public void removeLast(){ if(!formula.equals(""))formula=formula.substring(0, formula.length() - 1); }
}