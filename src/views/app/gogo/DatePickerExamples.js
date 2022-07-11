/* eslint-disable jsx-a11y/label-has-for */
/* eslint-disable jsx-a11y/label-has-associated-control */
import React, { useEffect, useState } from 'react';
import { Row, Card, CardBody, CardTitle, Input, Button, Col } from 'reactstrap';
import Select from 'react-select'
import DateTimePicker from 'react-datetime-picker';
import 'react-datepicker/dist/react-datepicker.css';
import { Colxx } from 'components/common/CustomBootstrap';
import ResultTable from '../operator/ResultTable';
import * as api from '../../../api'
import { NotificationManager } from 'components/common/react-notifications';
import CustomSelectInput from 'components/common/CustomSelectInput';
import { PieChart } from 'react-minimal-pie-chart';
const DatePickerExamples = () => {
  const [candidate, setCandidate] = useState({
    poll: "",
    fullname: "",
    imageSrc: ""
  })
  const [loading, setLoading] = useState(false)
  const [change, setChange] = useState(0)
  const [file, setFile] = useState(null)
  const [hideA, setHideA] = useState(true)
  const [polls, setPolls] = useState([])
  const [closedPolls, setClosedPolls] = useState([])
  const [manualAccreditation, setMA] = useState({
    pollId: "",
    userId: ""
  })
  const [img, setImg] = useState(null)
  const toggleHide = () => {
    setHideA(!hideA)
  }
  const changer = () => {
    setChange(Math.random())
  }
  const [users, setUsers] = useState([])
  const [blockedUsers, setBU] = useState([])
  const [poll, setPoll] = useState({
    title: "",
    openingDate: new Date(),
    closingDate: new Date()
  })
  useEffect(() => {
    api.getOpenPolls("adminActive")
      .then(r => setPolls(r.data.reverse()))
    api.getOpenPolls("adminClosed")
      .then(r => setClosedPolls(r.data.reverse()))
    api.getUsers(true)
      .then(r => setUsers(r.data))
    api.getUsers(false)
      .then(r => setBU(r.data))
    setCandidate({

      poll: "",
      fullname: "",
      imageSrc: ""
    }
    )
    setPoll({
      title: "",
      openingDate: new Date(),
      closingDate: new Date()
    })
    setMA({
      pollId: "",
      userId: ""
    })
  }, [change])
  const addAndEditPoll = () => {
    if (Date.parse(poll.openingDate) > Date.parse(poll.closingDate)) {
      NotificationManager.error("Close Date cannot preceed Start Date", "ERROR", 2000, null, null)
      return
    }
    if (poll.title === "") {
      NotificationManager.error("Title cannot be empty", "ERROR", 2000, null, null)
      return
    }
    setLoading(true)
    api.createAndEditPoll(poll)
      .then(r => {
        changer()
        setLoading(false)
        NotificationManager.success("Poll created successfully", "Success", 2000, null, null)
      })
      .catch(e => {
        setLoading(false)
        NotificationManager.error("Process Failed", "ERROR", 2000, null, null)
      })

  }
  const addAndEditCandidate = () => {
    setLoading(true)
    api.addAndEditCandidate(candidate)
      .then(r => {
        changer()
        setLoading(false)
        NotificationManager.success("Candidate Enlisted!", "Success", 2000, null, null)
      })
      .catch(e => {
        setLoading(false)
        NotificationManager.error("Process Failed", "ERROR", 2000, null, null)
      })
  }
  const enrollUser = () => {
    setLoading(true)
    api.enrollUser(manualAccreditation)
      .then(r => {
        changer()
        setLoading(false)
        NotificationManager.success("User Enrolled Successful", "Success", 2000, null, null)
      })
      .catch(e => {
        setLoading(false)
        NotificationManager.error("Process Failed", "ERROR", 2000, null, null)
      })
  }
  const uploadUsers = () => {
    const formdata = new FormData()
    formdata.append("file", file)
    setLoading(true)
    api.uploadUsers(manualAccreditation.pollId.value, formdata)
      .then(r => {
        changer()
        setLoading(false)
        NotificationManager.success("Upload Successful", "Success", 2000, null, null)
      })
      .catch(e => {
        setLoading(false)
        NotificationManager.error("Upload Failed", "ERROR", 2000, null, null)
      })
  }
  const downloadUsers = () => {
    setLoading(true)
    api.downloadUsers()
      .then(response => {
        setLoading(false)
        NotificationManager.success("Download Successful", "Success", 2000, null, null)
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', "file.csv"); //or any other extension
        document.body.appendChild(link);
        link.click();
      })

      .catch(e => {
        setLoading(false)
        NotificationManager.error("Download Failed", "ERROR", 2000, null, null)
      })
  }
  const defaultLabelStyle = {
    fontSize: '10px',
    fontFamily: 'sans-serif',
  };
  const shiftSize = 3;
  const toBase64 = file => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
  const test = (file) => {
    if (file === null) return;
    toBase64(file)
      .then(r => setCandidate({ ...candidate, imageSrc: r }))
  }

  return (
    <>
      <Row>
        <Colxx xxs="12" xl="8" className="mb-4">
          <Card>
            <CardBody>
              <CardTitle>
                Create New Poll
              </CardTitle>
              <label>
                Title
              </label>
              <div className="mb-5">
                <Input
                  type="text"
                  value={poll.title}
                  onChange={e => setPoll({ ...poll, title: e.target.value })}
                />
              </div>

              <Row className="mb-5">

                <Colxx xxs="6">
                  <label>
                    Opening Date & Time
                  </label>
                  <DateTimePicker
                    value={poll.openingDate}
                    onChange={e => setPoll({ ...poll, openingDate: e })}
                    format="dd-MM-y h:mm a"
                    calendarIcon={null}
                    clearIcon={null}
                  />
                </Colxx>
                <Colxx xxs="6">
                  <label>
                    Closing Date & Time
                  </label>
                  <DateTimePicker
                    value={poll.closingDate}
                    onChange={e => setPoll({ ...poll, closingDate: e })}
                    format="dd-MM-y h:mm a"
                    calendarIcon={null}
                    clearIcon={null}
                  />
                </Colxx>
              </Row>
              <Button
                className={`btn-shadow btn-multiple-state float-right ${loading ? 'show-spinner' : ''
                  }`}
                disabled={loading}
                onClick={addAndEditPoll} ><span className="spinner d-inline-block">
                  <span className="bounce1" />
                  <span className="bounce2" />
                  <span className="bounce3" />
                </span><spaan className="label">Create Poll</spaan></Button>
            </CardBody>
          </Card>
        </Colxx>
        <Colxx xxs="12" xl="4" className="mb-4">
          <Row>
            <Card>
              <CardBody>
                <PieChart
                  data={[
                    { title: 'Active Users', value: users.length, color: '#E38627' },
                    { title: 'Blocked Users', value: blockedUsers.length, color: '#37E2D5' },
                  ]}
                  radius={PieChart.defaultProps.radius - shiftSize}
                  segmentsShift={(index) => (index === 0 ? shiftSize : 0.5)}
                  label={({ dataEntry }) => dataEntry.value}
                  labelStyle={{
                    ...defaultLabelStyle,
                  }}
                />;
              </CardBody>
            </Card>

          </Row>
        </Colxx>
      </Row>
      <Row>
        <Colxx xxs="12" xl="8" className="mb-4">
          <Card>
            <CardBody>
              <CardTitle>
                Add candidate
              </CardTitle>
              <img src={candidate.imageSrc} className="h-30 w-30" /><br />
              <input className="mt-3" type='file' accept='image/jpg' onChange={e => { (e.target.files[0].size / 1024) > 50 ? NotificationManager.error("File size larger than 50KB", "Error", 2000) : setImg(URL.createObjectURL(e.target.files[0])); test(e.target.files[0].size / 1024 > 50 ? null : e.target.files[0]) }} /><i>Max size-50KB</i><br />
              <label className='mt-4'>
                Poll
              </label>
              <Select
                components={{ Input: CustomSelectInput }}
                className="react-select"
                classNamePrefix="react-select"
                name="form-field-name"
                options={polls.map((x, i) => {
                  return { label: x.title, value: x.id, key: i };
                })}
                value={candidate.poll}
                onChange={(val) => setCandidate({ ...candidate, poll: val })}
              />
              <label>
                Fullname
              </label>
              <div className="mb-5">
                <Input
                  type="text"
                  value={candidate.fullname}
                  onChange={e => setCandidate({ ...candidate, fullname: e.target.value })}
                />
              </div>


              <Button onClick={addAndEditCandidate} className={`btn-shadow btn-multiple-state float-right ${loading ? 'show-spinner' : ''
                }`}
                disabled={loading} ><span className="spinner d-inline-block"> <span className="bounce1" />
                  <span className="bounce2" />
                  <span className="bounce3" /></span><span className='label'>Add Candidate</span></Button>
            </CardBody>
          </Card>
        </Colxx>
        <Colxx xxs="12" xl="4" className="mb-4">
          <Row>
            <Card>
              <CardBody>
                <PieChart
                  data={[
                    { title: 'Open Polls', value: polls.length, color: '#1363DF' },
                    { title: 'Closed Polls', value: closedPolls.length, color: '#47B5FF' },
                  ]}
                  radius={PieChart.defaultProps.radius - shiftSize}
                  segmentsShift={(index) => (index === 0 ? shiftSize : 0.5)}
                  label={({ dataEntry }) => dataEntry.value}
                  labelStyle={{
                    ...defaultLabelStyle,
                  }}
                />;
              </CardBody>
            </Card>
          </Row>

        </Colxx>
      </Row>
      <Row>

        <Colxx xxs="12" xl="8" className="mb-4">
          <Card className="h-100">
            <CardBody>
              <CardTitle>
                Enroll Users
              </CardTitle>
              <label>
                Poll
              </label>
              <Select
                components={{ Input: CustomSelectInput }}
                className="react-select"
                classNamePrefix="react-select"
                name="form-field-name"
                options={polls.map((x, i) => {
                  return { label: x.title, value: x.id, key: i };
                })}
                value={manualAccreditation.pollId}
                onChange={(val) => setMA({ ...manualAccreditation, pollId: val })}
              />
              <br />
              <label>
                User (for manual enrolment)
              </label>

              <Select
                components={{ Input: CustomSelectInput }}
                className="react-select"
                classNamePrefix="react-select"
                name="form-field-name"
                options={users.reverse().map((x, i) => {
                  return { label: `${x.lastname} ${x.firstname}`, value: x.id, key: i };
                })}
                value={manualAccreditation.userId}
                onChange={(val) => setMA({ ...manualAccreditation, userId: val })}
              />
              <br />
              <Button onClick={enrollUser} className={`btn-shadow btn-multiple-state float-right ${loading ? 'show-spinner' : ''
                }`}
                disabled={loading}><span className="spinner d-inline-block"><span className="bounce1" />
                  <span className="bounce2" />
                  <span className="bounce3" /></span><span className='label'>Enroll User</span></Button>
            </CardBody>
          </Card>
        </Colxx>
        <Colxx xxs="12" xl="4" className="mb-4">
          <Card className="h-100">
            <CardBody>
              <CardTitle>
                Upload User Enrolment File (.csv) for {manualAccreditation.pollId.label}
              </CardTitle>
              <Input
                type="file"
                value={file === null ? "" : file.filename}
                accept="text/csv"
                onChange={e => setFile(e.target.files[0])}
              />
              <Button onClick={uploadUsers} className='float-right' disabled={manualAccreditation.pollId === "" || loading}>Upload</Button><br />
              <Button onClick={downloadUsers} className='mt-5 float-right' disabled ={loading}>Download Active User List</Button>
            </CardBody>
          </Card>
        </Colxx>
      </Row>
      <Button className="mr-5" onClick={toggleHide} disabled={loading}>{!hideA ? `Hide` : `Show`} User List</Button>
      {!hideA &&

        <Row>
          {users.length > 0 && <ResultTable data={users} update={changer} />}
          {blockedUsers.length > 0 && <ResultTable data={blockedUsers} update={changer} />}
        </Row>}
    </>
  );
};
export default (DatePickerExamples);
