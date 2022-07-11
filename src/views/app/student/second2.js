import React, { useContext, useEffect,useState } from 'react';
import { Row } from 'reactstrap';
import { Colxx, Separator } from 'components/common/CustomBootstrap';
import Breadcrumb from 'containers/navs/Breadcrumb';
import { context } from 'App';
import * as api from '../../../api'
import Tickets from './Tickets';

const Second2 = ({ match }) => {
  const [data, setDate] = useContext(context)
  const [poll,setPoll]=useState(null)
  useEffect(()=>{
    api.findInActivePolls(data.user.emailAddress)
    .then(r=>{setPoll(r.data)})
  },[])
  
  return (
    <>
      <Row>
        <Colxx xxs="12">
          <Breadcrumb heading="menu.second2" match={match} />
          <Separator className="mb-5" />
        </Colxx>
      </Row>
      <Row>
        <Colxx xxs="12" className="mb-4">
        <p>
          
            {poll!==null&&<Tickets data={poll} type={"view"}/>}
            {poll===null&&<b>No open poll at the moment</b>}
          </p>
        </Colxx>
      </Row>
      
      
      <div className='float-right'>
     
      </div>
     
    </>)
};
export default Second2;
