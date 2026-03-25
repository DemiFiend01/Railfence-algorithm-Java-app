package pl.rapala.magdalena.railfence6.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.io.Serializable;

/**
 * ResultHistory is a database unit class which stores the results of the cipher.
 * 
 * @author Magdalena Rapala
 * @version 1.2
 */
@Entity
public class ResultHistory implements Serializable {

    /**
     * serialVersionUID is a value required by the Serializable interface, checks compatible versions, conventionally 1L.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * rId is the ResultHistory record id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rId;

    /**
     * getId is a getter method which returns the id of the record.
     * 
     * @return rId the ResultHistory record id.
     */
    public Long getId() {
        return rId;
    }

    /**
     * setId is a method which sets the rId (id) for the CipherParams unit.
     * 
     * @param rId value of the new id
     */
    public void setId(Long rId) {
        this.rId = rId;
    }
    
    /**
     * cipherParams is a mapping to the appropriate cipherParams record (one-to-one).
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cipherId", referencedColumnName = "cId")
    private CipherParams cipherParams;
    
    /**
     * Get the value of cipherParams
     *
     * @return the value of cipherParams
     */
    public CipherParams getCipherParams() {
        return cipherParams;
    }

    /**
     * Set the value of cipherParams
     *
     * @param cipherParams new value of cipherparams
     */
    public void setCipherParams(CipherParams cipherParams) {
        //add checking whether it is correct
        this.cipherParams = cipherParams;
    }
    
    /**
     * result is the result of the cipher operation, either Encryption or Decryption.
     */
    private String result;
    
    /**
     * Get the value of result
     *
     * @return the value of result
     */
    public String getResult() {
        return result;
    }

    /**
     * Set the value of result
     *
     * @param result new value of result
     */
    public void setResult(String result) {
        //add checking whether it is correct
        this.result = result;
    }

    /**
     * hashCode is an overriden method which returns the hashed id.
     * 
     * @return hash - hashed code.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rId != null ? rId.hashCode() : 0);
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
        // TODO: Warning - this method won't work in the case the rId fields are not set
        if (!(object instanceof ResultHistory)) {
            return false;
        }
        ResultHistory other = (ResultHistory) object;
        if ((this.rId == null && other.rId != null) || (this.rId != null && !this.rId.equals(other.rId))) {
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
        return "pl.rapala.magdalena.railfence6.model.ResultHistory[ id=" + rId + " ]";
    }
    
}
