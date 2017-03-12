/* $Id: MemberInfo.java,v 1.2 2008/10/19 06:38:12 joelchou Exp $ */
package members.datamodel;

import java.io.Serializable;

import members.utils.Utility;

/**
 * This class is for member information. Member info does not need to link to an 
 * account since it can be used to maintain member info.
 * 
 * @author Cheng-Hung Chou
 * @since 1/26/2007
 *
 */
public class MemberInfo implements Serializable {
    private long memberId;
    private String firstName;
    private String lastName;
    private String chFirstName;
    private String chLastName;
    private String homePhone;
    private String cellPhone;
    private String workPhone;
    private String email;
    private AccountInfo accountInfo;

    public MemberInfo() {

    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getChFirstName() {
        return chFirstName;
    }

    public void setChFirstName(String chFirstName) {
        this.chFirstName = chFirstName;
    }

    public String getChLastName() {
        return chLastName;
    }

    public void setChLastName(String chLastName) {
        this.chLastName = chLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getChineseName() {
        return (Utility.isEmpty(chLastName) ? "" : chLastName)
            + (Utility.isEmpty(chFirstName) ? "" : chFirstName);
    }

    public String getOtherName() {
        return (Utility.isEmpty(firstName) ? "" : firstName + " ")
            + (Utility.isEmpty(lastName) ? "" : lastName);

    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }    
    
    /**
     * Gets a display name with the combination of Chinese name and other name.
     * 
     * @return
     */
    public String getName() {
        StringBuffer name = new StringBuffer();
        if(!Utility.isEmpty(getChineseName()))
            name.append(getChineseName());
        if(name.length() > 0)
            name.append(" ");
        if(!Utility.isEmpty(getOtherName()))
            name.append(getOtherName());

        return name.toString();
    }

    public boolean equals(Object obj) {
        boolean flag = false;
        MemberInfo theObj = (MemberInfo)obj;
        if(theObj != null)
            flag = theObj.getMemberId() == memberId;

        return flag;
    }

    public int hashCode() {
        return (int)memberId + homePhone.hashCode();
    }
}
