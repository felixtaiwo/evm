import { NotificationManager } from 'components/common/react-notifications';
import React,{useState} from 'react';
import {
  CustomInput,
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Input,
  Label,
} from 'reactstrap';
//import * as api from '../../../api'

const AddNewModal = ({ modalOpen, toggleModal, type="query",code }) => {
  const [file,setFile]=useState({name:""})
  const [matric,setMatric]=useState("")
  const [loading,setLoading]=useState(false)
  const [payload,setPayload]=useState({
    ca:"",
    exam:"",
    total:"",
    session:{
      session:{
        session:""
      }
    }
  })
  const updateResult=()=>{
    setLoading(true)
    // api.updateResult(payload)
    // .then(r=>{
    //   setLoading(false)
    //   NotificationManager.success("Result updated successfully","SUCCESS",2000,null,null)
    // })
    // .catch(e=>{
    //   setLoading(false)
    //   NotificationManager.error("Result updation failed","ERROR",2000,null,null)
    // })
  }
  const query=()=>{
    if(type==="query"){
      if(!matric || !code||!payload.session.session.session){
        NotificationManager.error("Empty Field(s)","ERROR",2000,null,null)
        return;
      }
      NotificationManager.info("Request sent, please wait","INFO",2000,null,null)
      setLoading(true)
      // api.findResult(matric,code)
      // .then(r=>{
      //   setLoading(false)
      //   for(const result of r.data){
      //     if(result.session.session.session===payload.session.session.session){
      //       NotificationManager.success("Result Found","Success",2000,null,null)
      //       setPayload(result)
      //       return
      //     } else{
      //       NotificationManager.error("No Result found for the specified session","ERROR",2000,null,null)
      //       return;
      //     }
      //   }
      //   NotificationManager.error("No Result Found","ERROR",2000,null,null)
      // })
      // .catch(e=>{
      //   setLoading(false)
      //   NotificationManager.error("No Result Found","ERROR",2000,null,null)
      //   return
      // })
    } else{
     if(file.type!=="text/csv"){
       window.scrollTo(0,0)
      NotificationManager.error("Only .csv extension file is allowed","ERROR",2000,null,null)
      return
     }
    //  setLoading(true)
     const formdata= new FormData()
     formdata.append("file",file)
    //  api.uploadResult(formdata)
    //  .then(r=>{
    //    setLoading(false)
    //    NotificationManager.success("Result upload successful","Success",5000,null,null)
    //  })
    //  .catch(e=>{
    //   setLoading(false)
    //   NotificationManager.error("Result upload failed","Error",5000,null,null)
    //  })
    }
  }
  return (
    <Modal
      isOpen={modalOpen}
      toggle={toggleModal}
      wrapClassName="modal-right"
      backdrop="static"
    >
      <ModalHeader toggle={toggleModal}>
       {type==="query"?"QUERY RESULT/CORRECTION":"UPLOAD RESULT"}
      </ModalHeader>
     { type!=="query"&&<ModalBody>
        
        <Label className="mt-4">
         <h3><b>Note:</b></h3>
        </Label>
        <Input type="textarea" name="text" id="exampleText" value={`Rows with irrelevant or invalid data types are ignored`}/>
        <Label className="mt-4">
          <b>Course Result File</b>
        </Label>
        <CustomInput
          type="file"
          id="exCustomRadio"
          name="customRadio"
          label="File"
          onChange={e=>setFile(e.target.files[0])}
        />
        <span><b>{file.name}</b></span>
      </ModalBody>}
     { type==="query"&&<ModalBody>
        
        <Label className="mt-4">
         Matric Number
        </Label>
        <Input type="text" name="text" id="exampleText" value={matric} onChange={e=>setMatric(e.target.value)}/>
        <Label className="mt-4">
         Course
        </Label>
        <Input type="text" name="text" id="exampleText" value={code} onChange={e=>setMatric(e.target.value)} disabled={true}/>
        <Label className="mt-4">
         Session
        </Label>
        <Input type="text" name="text" id="exampleText" value={payload.session.session.session} onChange={e=>setPayload({...payload,session:{session:{session:e.target.value}}})} />
        <Label className="mt-4">
         CA
        </Label>
        <Input type="text" name="text" id="exampleText" value={payload.ca} onChange={e=>setPayload({...payload,ca:e.target.value})} disabled={true}/>
        <Label className="mt-4">
         Exam
        </Label>
        <Input type="text" name="text" id="exampleText" value={payload.exam} onChange={e=>setPayload({...payload,exam:e.target.value})} disabled={true}/>
        <Label className="mt-4">
         Total
        </Label>
        <Input type="text" name="text" id="exampleText" value={payload.total} onChange={e=>setPayload({...payload,total:e.target.value})}/>
        
      </ModalBody>}
      <ModalFooter>
        <Button color="secondary" outline onClick={toggleModal}>
         Close
        </Button>
        <Button color="primary" onClick={query} disabled={loading}>
          {type==="query"?"Query":"Upload"}
        </Button>
        {type==="query"&&<Button color="warning" onClick={updateResult} disabled={loading}>
        Update Result
        </Button>}
      </ModalFooter>
    </Modal>
  );
};

export default AddNewModal;
