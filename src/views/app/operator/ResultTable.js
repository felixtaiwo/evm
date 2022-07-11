import React, { useState,useEffect } from 'react';
import classnames from 'classnames';
import { Button } from 'reactstrap';
import * as api from '../../../api'
import { NotificationManager } from 'components/common/react-notifications';

const ResultTable = ({ data,update}) => {
    const a =["S/N","Identtifier","Lastname","Firstname","Othername","Email Address","Phone","Toggle Block"]
    const headers =a
    const toggleBlock=(r)=>{
      api.toggleBlock(r)
      .then(r=>{
          update()
          NotificationManager.success("User Blocked","Successful",2000)
      })
      .catch(e=>{
        NotificationManager.error("Process Failed","Error",2000)
      })
    }

    return (
        <>
            <h4 className='float-left mt-3'><b>{`${data[0].blocked?"Inactive ":"Active "} Users`}</b></h4><br/>
            <table className={`r-table table ${classnames({ 'table-divided': true })}`}>
                {<thead>
                    <tr>
                   { headers.map(l=><th className={'sorted-asc'}>
                        {l}
                        </th>)}
                    </tr>
                </thead>}
                <tbody>
                   {data.map((r,k)=><tr>
                        <td>
                            {k+1}
                        </td>
                        <td>
                        {r.identifier}
                        </td>
                        <td>
                        {r.lastname}
                        </td>
                        <td>
                            {r.firstname}
                        </td>
                        <td>
                            {r.middlename}
                        </td>
                        <td>
                            {r.emailAddress}
                        </td>
                        <td>
                            {r.phone}
                        </td>
                      <td>
                            <Button color={r.blocked?"secondary":"danger"} onClick={()=>toggleBlock(r.emailAddress)}>{r.blocked?"Activate":"Deactivate"} </Button>
                        </td>
                       
                        
                    </tr>)}
                    
                </tbody>
            </table>
            {/* <AddNewModal modalOpen={open} toggleModal={toggle} code={code} type={type}/> */}
            </>
    )

}
export default ResultTable;