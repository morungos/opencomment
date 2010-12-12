/**
 * FeedbackRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.org.opencomment.service;

public class FeedbackRequest  implements java.io.Serializable {
    private java.lang.String questionId;

    private java.lang.String descriptorUrl;

    private java.lang.String submission;

    private int attempt;

    public FeedbackRequest() {
    }

    public FeedbackRequest(
           java.lang.String questionId,
           java.lang.String descriptorUrl,
           java.lang.String submission,
           int attempt) {
           this.questionId = questionId;
           this.descriptorUrl = descriptorUrl;
           this.submission = submission;
           this.attempt = attempt;
    }


    /**
     * Gets the questionId value for this FeedbackRequest.
     * 
     * @return questionId
     */
    public java.lang.String getQuestionId() {
        return questionId;
    }


    /**
     * Sets the questionId value for this FeedbackRequest.
     * 
     * @param questionId
     */
    public void setQuestionId(java.lang.String questionId) {
        this.questionId = questionId;
    }


    /**
     * Gets the descriptorUrl value for this FeedbackRequest.
     * 
     * @return descriptorUrl
     */
    public java.lang.String getDescriptorUrl() {
        return descriptorUrl;
    }


    /**
     * Sets the descriptorUrl value for this FeedbackRequest.
     * 
     * @param descriptorUrl
     */
    public void setDescriptorUrl(java.lang.String descriptorUrl) {
        this.descriptorUrl = descriptorUrl;
    }


    /**
     * Gets the submission value for this FeedbackRequest.
     * 
     * @return submission
     */
    public java.lang.String getSubmission() {
        return submission;
    }


    /**
     * Sets the submission value for this FeedbackRequest.
     * 
     * @param submission
     */
    public void setSubmission(java.lang.String submission) {
        this.submission = submission;
    }


    /**
     * Gets the attempt value for this FeedbackRequest.
     * 
     * @return attempt
     */
    public int getAttempt() {
        return attempt;
    }


    /**
     * Sets the attempt value for this FeedbackRequest.
     * 
     * @param attempt
     */
    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FeedbackRequest)) return false;
        FeedbackRequest other = (FeedbackRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.questionId==null && other.getQuestionId()==null) || 
             (this.questionId!=null &&
              this.questionId.equals(other.getQuestionId()))) &&
            ((this.descriptorUrl==null && other.getDescriptorUrl()==null) || 
             (this.descriptorUrl!=null &&
              this.descriptorUrl.equals(other.getDescriptorUrl()))) &&
            ((this.submission==null && other.getSubmission()==null) || 
             (this.submission!=null &&
              this.submission.equals(other.getSubmission()))) &&
            this.attempt == other.getAttempt();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getQuestionId() != null) {
            _hashCode += getQuestionId().hashCode();
        }
        if (getDescriptorUrl() != null) {
            _hashCode += getDescriptorUrl().hashCode();
        }
        if (getSubmission() != null) {
            _hashCode += getSubmission().hashCode();
        }
        _hashCode += getAttempt();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FeedbackRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.opencomment.org.uk/opencomment/", "FeedbackRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("questionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "questionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descriptorUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descriptorUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("submission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "submission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attempt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attempt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
