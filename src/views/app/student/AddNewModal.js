import React, { useContext, useEffect, useState } from 'react';
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from 'reactstrap';
import ImageCards from './ImageCards';
import { NavLink } from 'react-router-dom';
import Countdown from 'react-countdown';
import * as api from '../../../api'
import { context } from 'App';
import { NotificationManager } from 'components/common/react-notifications';
import OtpInput from 'react-otp-input';

const AddNewModal = ({ modalOpen, toggleModal, data, type }) => {
  const [expires, setExpires] = useState(null)
  const [userData, setUserData] = useContext(context)
  const [poll, setPoll] = useState({
    email: userData.user.emailAddress,
    poll: data,
    token: ""
  })
  const [loading, setLoading] = useState(false)
  const tokenGen = () => {
    setLoading(true)
    api.resendToken(userData.user.emailAddress)
      .then(r => {
        setLoading(false)
        setExpires(r.data)
      })
  }
  useEffect(() => {
    if (!modalOpen) {
      setTimer(false)
    }
    if (poll.token.length >= 6 && !loading) {
      NotificationManager.info("Ballot Casting", "Info", 2000)
      setLoading(true)
      api.voteNow(poll)
        .then(r => {
          setPoll({ ...poll, token: "" })
          setLoading(false)
          NotificationManager.success("Ballot casted", "Success", 2000)
          location.replace('/app/user/dashboard')
        })
        .catch(e => {
          setPoll({ ...poll, token: "" })
          setLoading(false)
          NotificationManager.error(e.response.data, "Error", 2000)
        })

    }
  })
  const [timer, setTimer] = useState(false)
  const voteNow = (id) => {
    setTimer(true)
    setPoll({ ...poll, candidate: id, poll: data.id })
  }
  const timerCompleted = () => {
    setExpires(null)
  }

  return (
    <Modal
      isOpen={modalOpen}
      toggle={toggleModal}
      wrapClassName="modal-right"
      backdrop="static"
    >
      <ModalHeader toggle={toggleModal}>
        {data && data.title.toUpperCase()}
      </ModalHeader>
      <ModalBody>
        {data !== undefined && data.candidates.map((candidate, key) => <> <NavLink to="#" onClick={type === undefined ? () => voteNow(candidate.id) : ""}>
          {!timer && <ImageCards data={candidate} poll={data} />}
        </NavLink></>)}
        {timer && <center>
          {expires !== null && <Countdown date={expires} intervalDelay={0} className={"text-primary"} onComplete={timerCompleted} />} <br />
          
          
            <OtpInput className='w-40 mb-5 p-1'
            value={poll.token}
            onChange={e => setPoll({ ...poll, token: e.toUpperCase()})}
            numInputs={6}
            separator={<span>{"   "}</span>}
            shouldAutoFocus={true}
            isDisabled={loading}
            isInputSecure={true}
          /><br/>


          <Button
            color="primary"
            className={`btn-shadow btn-multiple-state ${loading ? 'show-spinner' : ''
              }`}
            size="lg"
            onClick={tokenGen}
            disabled={loading}
          >
            <span className="spinner d-inline-block">
              <span className="bounce1" />
              <span className="bounce2" />
              <span className="bounce3" />
            </span>
            <span className="label">
              {expires === null ? "Send Token" : "Resend Token"}
            </span>
          </Button>

        </center>}

      </ModalBody>
      <ModalFooter>
        <Button color="secondary" outline onClick={toggleModal}>
          Close
        </Button>

      </ModalFooter>
    </Modal>
  );
};

export default AddNewModal;
