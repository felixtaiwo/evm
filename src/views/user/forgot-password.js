import React, { useState } from 'react';
import { Row, Card, CardTitle, Label, FormGroup, Button } from 'reactstrap';
import { NavLink } from 'react-router-dom';
import { Formik, Form, Field } from 'formik';
import { Colxx } from 'components/common/CustomBootstrap';
import IntlMessages from 'helpers/IntlMessages';
import { NotificationManager } from 'components/common/react-notifications';
import * as api from '../../api'



const ForgotPassword = () => {
const[loading,setLoading]=useState(false)
const[id,setId]=useState("")
  const onForgotPassword = () => {
    if (!loading) {
      setLoading(true)
      api.resetPassword(id)
      .then(r=>{
        setLoading(true)
        NotificationManager.success("Reset Successful","Success",2000,null,null)
        location.replace("/")
      }) 
      .catch(e=>{
        setLoading(false)
        NotificationManager.error("Reset failed, please check your details","Error",2000,null,null)
      })
    }
  };

  

  return (
    <Row className="h-100">
      <Colxx xxs="12" md="10" className="mx-auto my-auto">
        <Card className="auth-card">
          <div className="position-relative image-side ">
            
          </div>
          <div className="form-side">
            <NavLink to="/" className="white">
              <span className="logo-single" />
            </NavLink>
            <CardTitle className="mb-4">
             <b>FORGOT PASSWORD</b>
            </CardTitle>

            <Formik>
              {() => (
                <Form className="av-tooltip tooltip-label-bottom">
                  <FormGroup className="form-group has-float-label">
                    <Label>
                     Email / Phone
                    </Label>
                    <Field
                      className="form-control"
                      name="email"
                      value={id}
                      onChange={e=>setId(e.target.value)}
                    />
                    
                  </FormGroup>

                  <div className="d-flex justify-content-between align-items-center">
                    <NavLink to="/user/forgot-password">
                      <IntlMessages id="user.forgot-password-question" />
                    </NavLink>
                    {id.length>=1&&<Button
                      color="primary"
                      className={`btn-shadow btn-multiple-state ${
                        loading ? 'show-spinner' : ''
                      }`}
                      size="lg"
                      onClick={onForgotPassword}
                    >
                      <span className="spinner d-inline-block">
                        <span className="bounce1" />
                        <span className="bounce2" />
                        <span className="bounce3" />
                      </span>
                      <span className="label">
                        <IntlMessages id="user.reset-password-button" />
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


export default (ForgotPassword);
