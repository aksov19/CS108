public class Metropolis {
    private String metropolis;
    private String continent;
    private Long population;

    public Metropolis(String metropolis, String continent, Long population){
        this.metropolis = metropolis;
        this.continent = continent;
        this.population = population;
    }

    public Long getPopulation() {
        return population;
    }

    public String getMetropolis() {
        return metropolis;
    }

    public String getContinent() {
        return continent;
    }
}
