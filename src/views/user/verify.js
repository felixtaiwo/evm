import React, { useState,useEffect } from 'react';
import { Row, Card, CardTitle, Label, FormGroup, Button } from 'reactstrap';
import { NavLink, useParams } from 'react-router-dom';
import { Formik, Form, Field } from 'formik';
import { Colxx } from 'components/common/CustomBootstrap';
import { NotificationManager } from 'components/common/react-notifications';
import * as api from '../../api'

const Verify = () => {
  const [loading, setLoading] = useState(false)
  const [active,setActive]=useState(false)
  const { coded } = useParams()
  const [valid, setValid] = useState(false)
  const [token, setToken] = useState(
    {
      tok: "",
      user: atob(coded),
      password: "",
      cpassword: ""
    }
  );
  useEffect(()=>{
    if(valid){
      if(token.password===token.cpassword && token.password.length>=8) setActive(true) 
      else setActive(false)
    } else{
        if(token.tok.length>=1) setActive(true)
        else setActive(false)
    }
  })
  const confirmToken = () => {
    if (!loading) {
      setLoading(true)
      if (valid) {
        api.choosePassword(token)
        .then(r=>{
          setLoading(false)
          NotificationManager.success("Password set","Success",2000,null,null,null)
          location.replace("/user/login")
        })
        .catch(e=>{
          setLoading(false)
          NotificationManager.error("Error","Error",2000,null,null,null)
        })
      } else {
        if (token.tok !== '' && token.user !== '') {
          api.verify(token)
            .then(r => {
              setLoading(false)
              setValid(true)
              NotificationManager.success("Verification Successful", "Success", 2000, null, null);
            })
            .catch(e => {
              setLoading(false)
              NotificationManager.error("Verification Failed", "Error", 2000, null, null)
            })
        } else {
          NotificationManager.error("Invalid entry", "Error", 2000, null, null)
        }
      }
    }
  };


  return (
    <Row className="h-100">
      <Colxx xxs="12" md="10" className="mx-auto my-auto">
        <Card className="auth-card">
          <div className="position-relative image-side ">

          </div>
          <div className="form-side">

            <CardTitle className="mb-4">
              <b>{!valid ? "VERIFY ACCOUNT" : "CHOOSE PASSWORD"}</b>
            </CardTitle>

            <Formik>
              {({ errors, touched }) => (
                <Form className="av-tooltip tooltip-label-bottom">
                  {!valid && <FormGroup className="form-group has-float-label">
                    <Label>
                      Token
                    </Label>
                    <Field
                      className="form-control"
                      name="token"
                      value={token.tok}
                      onChange={e => setToken({ ...token, tok: e.target.value })}
                      type="text"
                    />

                  </FormGroup>}
                  {valid && <><FormGroup className="form-group has-float-label">
                    <Label>
                      Password
                    </Label>
                    <Field
                      className="form-control"
                      name="token"
                      value={token.password}
                      onChange={e => setToken({ ...token, password: e.target.value })}
                      type="password"
                    />
                  <i className='Red'>password must be at least 8 characters</i>
                  </FormGroup>
                    <FormGroup className="form-group has-float-label">
                      <Label>
                        Confirm Password
                      </Label>
                      <Field
                        className="form-control"
                        name="token"
                        value={token.cpassword}
                        onChange={e => setToken({ ...token, cpassword: e.target.value })}
                        type="password"
                      />

                    </FormGroup></>}

                  <div className="d-flex justify-content-between align-items-center">
                    <NavLink to="/user/forgot-password">

                    </NavLink>
                    {active&&<Button
                      color="primary"
                      className={`btn-shadow btn-multiple-state ${loading ? 'show-spinner' : ''
                        }`}
                      size="lg"
                      onClick={confirmToken}
                    >
                      <span className="spinner d-inline-block">
                        <span className="bounce1" />
                        <span className="bounce2" />
                        <span className="bounce3" />
                      </span>
                      <span className="label">
                        {!valid ? "Verify" : "Submit"}
                      </span>
                    </Button>}
                  </div>
                </Form>
              )}
            </Formik>
          </div>
        </Card>
      </Colxx>
    </Row>
  );
};

export default Verify
