package com.example.newyearresolution.classes;

public class Resolution {



    private Long idResolution;
    private String frequence;
    private int nbOccurence;
    private String action;

    public Resolution(Long idResolution,String action, String frequence, int nbOccurence) {
        this.frequence = frequence;
        this.nbOccurence = nbOccurence;
        this.action = action;
        this.idResolution=idResolution;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public int getNbOccurence() {
        return nbOccurence;
    }

    public void setNbOccurence(int nbOccurence) {
        this.nbOccurence = nbOccurence;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getIdResolution() { return idResolution; }

    public void setIdResolution(Long idResolution) { this.idResolution = idResolution; }

    @Override
    public String toString() {
        return "Resolution{" +
                "idResolution=" + idResolution +
                ", frequence='" + frequence + '\'' +
                ", nbOccurence=" + nbOccurence +
                ", action='" + action + '\'' +
                '}';
    }
}
