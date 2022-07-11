/* eslint-disable react/no-array-index-key */
import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import PerfectScrollbar from 'react-perfect-scrollbar';
import { Card, CardBody, CardTitle } from 'reactstrap';
import AddNewModal from './AddNewModal';
import Countdown from 'react-countdown';
const Tickets = ({ data, type }) => {
  const [modal, setModal] = useState(false)
  const [pollData, setPollData] = useState()
  const toggleModal = () => {
    setModal(false)
  }
  const pollClick = (data) => {
    setPollData(data)
    setModal(true)
  }
  return (<>
    <Card>
      <CardBody>
        <CardTitle>
          <b>{type === undefined ? "Currently in progress..." : type==="admin"?"ALL POLLS":"Your Polls"}</b>
        </CardTitle>
        <div className="dashboard-list-with-user">
          <PerfectScrollbar
            options={{ suppressScrollX: true, wheelPropagation: false }}
          >
            {data.map((ticket, index) => {
              return (
                <div
                  key={index}
                  className="d-flex flex-row mb-3 pb-3 border-bottom"
                >


                  <div className="pl-3 pr-5">
                    <NavLink to="#" onClick={() => pollClick(ticket)}>
                      {/* <p className="font-weight-medium mb-0 ">{ticket.title.toUpperCase()}</p> */}
                      <p className="font-weight-medium mb-0 ">{ticket.title.toUpperCase()}</p>
                      <p className="text-muted mb-0 text-small">
                        {new Date().getTime() >= new Date(ticket.openingDate).getTime() ? "Opened" : "Opens"} at {ticket.openingDate}
                      </p>
                    </NavLink>
                  </div>
                  <div className="pl-3 pr-5">
                    {new Date().getTime() >= new Date(ticket.closingDate).getTime() ? <>
                      <b className='text-primary'>Closed</b>
                      <p className="text-muted mb-0 text-small">
                        {ticket.closingDate}
                      </p>
                    </> : <>
                      Ends in <Countdown date={ticket.closingDate} intervalDelay={0} className={"text-primary"} />

                    </>}


                  </div>

                </div>
              );
            })}
          </PerfectScrollbar>
        </div>
      </CardBody>
    </Card>
    <AddNewModal modalOpen={modal} toggleModal={toggleModal} data={pollData} type={type} />
  </>);
};
export default Tickets;
