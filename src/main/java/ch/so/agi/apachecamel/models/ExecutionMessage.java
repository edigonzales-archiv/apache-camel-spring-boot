package ch.so.agi.apachecamel.models;

import java.util.Date;

public class ExecutionMessage {
    private Date importdate;
    private String datasetname;
    private String status;
    private Date landRegisterEntryDate;
    private Date journalEntryDate;
    private String referenceNumber;
    private String identnd;
    
    public Date getImportdate() {
        return importdate;
    }
    public void setImportdate(Date importdate) {
        this.importdate = importdate;
    }
    public String getDatasetname() {
        return datasetname;
    }
    public void setDatasetname(String datasetname) {
        this.datasetname = datasetname;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getLandRegisterEntryDate() {
        return landRegisterEntryDate;
    }
    public void setLandRegisterEntryDate(Date landRegisterEntryDate) {
        this.landRegisterEntryDate = landRegisterEntryDate;
    }
    public Date getJournalEntryDate() {
        return journalEntryDate;
    }
    public void setJournalEntryDate(Date journalEntryDate) {
        this.journalEntryDate = journalEntryDate;
    }
    public String getReferenceNumber() {
        return referenceNumber;
    }
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    public String getIdentnd() {
        return identnd;
    }
    public void setIdentnd(String identnd) {
        this.identnd = identnd;
    }   
}
