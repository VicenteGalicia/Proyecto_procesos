package Batalla.Datos.stats;

public class PlayerStat {
    String name = new String();
    float maxValue;
    float currentValue;

    public PlayerStat(String name, float maxValue, float initValue){
        this.name = name;
        this.maxValue = maxValue;
        this.currentValue = initValue;
    }

    public String getName(){
        return name;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getCurrentValue(){
        return currentValue;
    }

    public void setMaxValue(float newMaxValue){
        maxValue = newMaxValue;
    }

    public void setCurrentValue(float newCurrentValue){
        currentValue = newCurrentValue;
    }

    public float getPorcentage(){
        return (float)(currentValue / maxValue);    
    }

    @Override
    public String toString(){
        return name + "= [\n" +  
            "Valor Actual: " + currentValue +
            "\n]"; 
    }
}
