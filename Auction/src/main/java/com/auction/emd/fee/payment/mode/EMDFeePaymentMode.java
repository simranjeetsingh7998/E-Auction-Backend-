package com.auction.emd.fee.payment.mode;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "emdFeePaymentMode", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"emdfpMode"})
})
@Data
public class EMDFeePaymentMode implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -147729460816340679L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30)
	private String emdfpMode;
	private boolean isActive;
	
    public EMDFeePaymentModeVO emdFeePaymentModeToEMDFeePaymentModeVO() {
    	EMDFeePaymentModeVO emdFeePaymentModeVO = new EMDFeePaymentModeVO();
    	emdFeePaymentModeVO.setActive(isActive);
    	emdFeePaymentModeVO.setEmdfpMode(emdfpMode);
    	emdFeePaymentModeVO.setId(id);
      return emdFeePaymentModeVO;	
    }


}
