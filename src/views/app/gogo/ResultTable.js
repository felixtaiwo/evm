import React, { useEffect, useState } from 'react';
import classnames from 'classnames';
import { Row, Input, Button, CustomInput } from 'reactstrap';
import { Colxx, Separator } from 'components/common/CustomBootstrap';
import Breadcrumb from 'containers/navs/Breadcrumb';
import * as api from '../../../api'
import Tickets from '../student/Tickets';

const ResultTable = ({ match }) => {
    const [polls,setPolls]=useState([])
    useEffect(()=>{
        api.getOpenPolls("Admin")
        .then(r=>setPolls(r.data.reverse()))
    },[])
  
    return (
        <>

            <Row>
                <Colxx xxs="12">
                    <Breadcrumb heading="menu.results" match={match} />
                    <Separator className="mb-5" />
                </Colxx>
            </Row>
        
            <>
               
            <Tickets data={polls} type={"admin"}/>
            </>
        </>
    )

}
export default ResultTable;