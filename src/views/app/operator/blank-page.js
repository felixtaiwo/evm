import React,{useContext} from 'react';
import { Row } from 'reactstrap';
import { Colxx, Separator } from 'components/common/CustomBootstrap';
import Breadcrumb from 'containers/navs/Breadcrumb';
import { context } from 'App';
import ResultTable from './ResultTable';
const greet = () => {
  const t = new Date().getHours();
  if (t < 12) return "Good Morning";
  if (t > 12 && t < 16) return "Good Afternoon";
  return "Good Evening"
}
const BlankPage = ({ match }) => {

  const [data,setData]=useContext(context)
  return (
    <>
      <Row>
        <Colxx xxs="12">
          <Breadcrumb heading="menu.blank-page" match={match} />
          <Separator className="mb-5" />
        </Colxx>
      </Row>
      <Row>
        <Colxx xxs="12" className="mb-4">
          <p>
           <h2>{greet()}, {data.odata.firstname}</h2>
           
          </p>
          <ResultTable data={data.odata.courseList}/>
          
        </Colxx>
      </Row>
    </>
  );
};

export default BlankPage;
