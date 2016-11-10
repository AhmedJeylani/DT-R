package com.app.jj.digitalreceipt;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Jey on 01/07/2016.
 */
public class ReceiptData implements Serializable{

    private String _receiptName, _companyName, _imageLoc, _imageText;


    ReceiptData(String receiptName,String companyName,File imageFile,String imageText) {

        this._receiptName = receiptName;
        this._companyName = companyName;
        this._imageLoc = imageFile.getAbsolutePath();
        this._imageText = imageText;

    }

    public String getReceiptName() {
        return _receiptName;
    }

    public void setReceiptName(String name) {
        _receiptName = name;
    }

    public String getCompanyName() {
        return _companyName;
    }
    public void setCompanyName(String companyName) {
        _companyName = companyName;
    }

    public String getImageFileLocation() {
        return _imageLoc;
    }

    public void setimageFileLocation(String imageFileLocation) {
        _imageLoc = imageFileLocation;
    }

    public String getImageText() {
        return _imageText;
    }

    public void setImageText(String imageText) {
        _imageText = imageText;
    }

}
