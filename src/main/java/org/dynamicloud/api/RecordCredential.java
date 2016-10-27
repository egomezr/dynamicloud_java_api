package org.dynamicloud.api;

/**
 * This class has two attributes CSK and ACI keys.  This information is provided at moment tha registration in Dynamicloud.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/25/15
 **/
public class RecordCredential {
    private String csk;
    private String aci;

    /**
     * Constructs a credential using csk and aci params.
     * The best practice is either to store this information in a file or pass these keys via JVM params
     * to ensure that this keys keep secure.
     *
     * @param csk Client Secret Key
     * @param aci API Client ID
     */
    public RecordCredential(String csk, String aci) {
        this.csk = csk;
        this.aci = aci;
    }

    public String getCsk() {
        return csk;
    }

    public void setCsk(String csk) {
        this.csk = csk;
    }

    public String getAci() {
        return aci;
    }

    public void setAci(String aci) {
        this.aci = aci;
    }
}