import React, { useState, useEffect, useContext } from 'react';
import { Row } from 'reactstrap';

import * as api from '../../../api'
import { Colxx, Separator } from 'components/common/CustomBootstrap';
import Breadcrumb from 'containers/navs/Breadcrumb';
import { context } from 'App';
import Tickets from './Tickets';


const greet = () => {
  const t = new Date().getHours();
  if (t < 12) return "Good Morning";
  if (t >= 12 && t <= 15) return "Good Afternoon";
  return "Good Evening"
}

const Second = ({ match }) => {
  const [data, setData] = useContext(context)
  useEffect(()=>{
    api.findActivePolls(data.user.emailAddress)
    .then(r=>{setPoll(r.data)})
  },[])
  const [poll,setPoll]=useState(null)
  return (
    <>
      <Row>
        <Colxx xxs="12">
          <Breadcrumb heading="menu.second1" match={match} />
          <Separator className="mb-5" />
        </Colxx>
      </Row>
      <Row>
        <Colxx xxs="12" className="mb-4">
          <p>
            <h2>{greet()}, {data.user.firstname}</h2>
            {poll!==null && poll.length>0?<Tickets data={poll}/>:<b>Oops! No open poll at the moment</b>}
            
          </p>
        </Colxx>
      </Row>
    </>)
};
export default Second;
