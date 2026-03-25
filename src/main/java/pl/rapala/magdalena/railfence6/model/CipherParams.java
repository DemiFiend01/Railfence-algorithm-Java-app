package pl.rapala.magdalena.railfence6.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.io.Serializable;

/**
 * CipherParams is a database entity which stores the basic cipher parameters except for the result.
 * 
 * @author Magdalena Rapala
 * @version 1.1
 */
@Entity
public class CipherParams implements Serializable {

    /**
     * serialVersionUID is a value required by the Serializable interface, checks compatible versions, conventionally 1L.
     */
    private static final long serialVersionUID = 1L;
    /**
     * cId is the CipherParams record id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cId;

    /**
     * getId is a getter method which returns the id of the record.
     * 
     * @return cId the cipher record id.
     */
    public Long getId() {
        return cId;
    }
    
    /**
     * setId is a method which sets the cId (id) for the CipherParams unit.
     * 
     * @param cId value of the new id
     */
    public void setId(Long cId) {
        this.cId = cId;
    }

    /**
     * resultHistory is a mapping to the appropriate ResultHistory record (one-to-one).
     */
    @OneToOne(mappedBy = "cipherParams")
    private ResultHistory resultHistory;
    
    /**
     * modeSelect - either en - encoding or de - decoding.
     */
    private String modeSelect;
    /**
     * textCipher - the text to either encode or decode.
     */
    private String textCipher;
    /**
     * the number of rails needed for the cipher.
     */
    private int noOfRails;
    
    /**
     * Get the value of resultHistory
     *
     * @return the value of resultHistory
     */
    public ResultHistory getResultHistory() {
        return resultHistory;
    }

    /**
     * Set the value of resultHistory
     *
     * @param resultHistory new value of resultHistory
     */
    public void setResultHistory(ResultHistory resultHistory) {
        //add checking whether it is correct
        this.resultHistory = resultHistory;
    }
    
    /**
     * Get the value of modeSelect
     *
     * @return the value of modeSelect
     */
    public String getModeSelect() {
        return modeSelect;
    }

    /**
     * Set the value of modeSelect
     *
     * @param mode new value of modeSelect
     */
    public void setModeSelect(String mode) {
        //add checking whether it is correct
        this.modeSelect = mode;
    }
    
    /**
     * Get the value of textCipher
     *
     * @return the value of modeSelect
     */
    public String getTextCipher() {
        return textCipher;
    }

    /**
     * Set the value of textCipher
     *
     * @param text new value of textCipher
     */
    public void setTextCipher(String text) {
        //add checking whether it is correct
        this.textCipher = text;
    }
    
    /**
     * Get the value of noOfRails
     *
     * @return the value of noOfRails
     */
    public int getNoOfRails() {
        return noOfRails;
    }

    /**
     * Set the value of noOfRails
     *
     * @param number new value of noOfRails
     */
    public void setNoOfRails(int number) {
        //add checking whether it is correct
        this.noOfRails = number;
    }
    
    /**
     * hashCode is a method which returns the hashed id.
     * 
     * @return hash - hashed code.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cId != null ? cId.hashCode() : 0);
        return hash;
    }
    
    /**
     * equals is an overriden method which checks whether two objects are equal based on their id.
     * 
     * @param object to be compared.
     * @return boolean value.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CipherParams)) {
            return false;
        }
        CipherParams other = (CipherParams) object;
        if ((this.cId == null && other.cId != null) || (this.cId != null && !this.cId.equals(other.cId))) {
            return false;
        }
        return true;
    }

    /**
     * toString is a method which returns the id to the record as a String value.
     * 
     * @return id as a String.
     */
    @Override
    public String toString() {
        return "pl.rapala.magdalena.railfence5.model.CipherParams[ id=" + cId + " ]";
    }
    
}
