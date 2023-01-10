<#include "main_body.ftl" />
<@mainPage title="PDf version">
    <div class="main-outer-body">
        <div class="main-body">

        <div>
            <h2>Company Compliance Profile of  ${companyName}</h2>
<#--            <span style="float: right">Date: 03/03/2021</span>-->
        </div>


        <div>
            <div>Corporate Particulars of: </div>
            <ul>
                <li>BN/RC/IT: 	${rcNumber}</li>
                <li>Company Name: 	${companyName}</li>
                <li>Incorporation Date:	03/03/2021</li>
                <li>Company Type: BUSINESS NAME</li>
                <li>Status: ACTIVE</li>
                <li>Status Date: 03/03/2021</li>
            </ul>
        </div>


        <#list affiliates as y>
            <div class="inner-title-div">
                <span class="inner-title">${y.type} INFORMATION</span>
            </div>

            <table id="type-information">
            <thead>
                <tr>
                    <th>S/N</th>
                    <th>Firstname</th>
                    <th>Lastname</th>
                    <th>Gender</th>
                    <th>Email</th>
                    <th>Phone Number</th>
                    <th>DOB</th>
                    <th>Address</th>
                </tr>
            </thead>
            <tbody>
            <#list y.affiliateResponsePojoList as x>
                <tr>

                    <td>
                        <span>${x?index + 1}</span>
                    </td>

                    <td>
                        <#if x.firstname??>
                            <span class="inner-table-content">${x.firstname}</span>
                        </#if>
                    </td>

                    <td>
                        <#if x.surname??>
                            <span class="inner-table-content">${x.surname}</span>
                        </#if>
                    </td>

                    <td>
                        <#if x.type??>
                            <span class="inner-table-content">${x.type}</span>
                        </#if>
                    </td>

                    <td>
                        <#if x.email??>
                            <span class="inner-table-content">${x.email}</span>
                        </#if>
                    </td>

                    <td>
                        <#if x.phoneNumber??>
                            <span class="inner-table-content">${x.phoneNumber}</span>
                        </#if>
                    </td>



                    <td>
                        <#if x.dob??>
                            <span class="inner-table-content">DOB:  ${x.dob} </span>
                        </#if>
                    </td>

                    <td>
                        <#if x.address??>
                            <span class="inner-table-content">${x.address} </span>
                        </#if>
                    </td>

                </tr>
            </#list>
            </tbody>
        </table>
        </#list>

    </div>
  </div>
</@mainPage>