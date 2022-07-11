import React from 'react';
import {
  CardSubtitle,
  Row,
  Card,
  CardBody,
  CardTitle,
  CardImg,
  Badge,
} from 'reactstrap';
import { Colxx } from 'components/common/CustomBootstrap';

const ImageCards = ({ data,type,poll }) => {
  const percentageCalc=(x)=>{
    var total =0;
    for(let d of poll.candidates){
      total=total+d.voteCount
    }
    var a= (x/total)*100;
    if(isNaN(a)){
      return 0;
    } else if(x%total ===0)return a
    else return a.toFixed(2)
  }
  return (
    <Row>
      <Colxx xxs="12">
        <CardTitle className="mb-4">
          <b>{`${data.firstname} ${data.lastname}`}</b>
        </CardTitle>
        <Row>
          <Colxx >
            <Card className="mb-4">
              <div className="position-relative">
                <CardImg
                  top
                  src={data.imageSrc}
                  alt={`${data.firstname} ${data.lastname}`}
                />
                <Badge
                  color="info"
                  pill
                  className="position-absolute badge-top-left mx-2"
                >
                 {data.voteCount} votes
                </Badge>
                <Badge
                  color="secondary"
                  pill
                  className="position-absolute badge-top-left-2 mx-2"
                >
                  {percentageCalc(data.voteCount)}%
                </Badge>
                {poll.winner!==null&&poll.winner.id===data.id&&<Badge
                  color="danger"
                  pill
                  className="position-absolute badge-top-left-3 mx-2"
                >
                  Winner
                </Badge>}
              </div>
              <CardBody>
                <CardSubtitle className="mb-4">
                  {`${data.firstname} ${data.lastname}`}
                </CardSubtitle>
              </CardBody>
            </Card>
          </Colxx>

        </Row>
      </Colxx>
    </Row>
  );
};

export default ImageCards;
