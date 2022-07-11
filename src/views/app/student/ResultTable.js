import React from 'react';
import classnames from 'classnames';

const ResultTable = ({ data }) => {
    const headers =["Course Code","Course Unit","Score","Grade"]
    const getSemesters=(f)=>{
        var a=[]
        const keys =Object.keys(f)
        for(const k of keys){
            if(Array.isArray(f[k]) && f[k].length>0) a=[...a,[k,f[k]]]
        } const getSemesters=(f)=>{
        var a=[]
        const keys =Object.keys(f)
        for(const k of keys){
            if(Array.isArray(f[k]) && f[k].length>0) a=[...a,[k,f[k]]]
        }
        return a;
    }
    const getSummary=(k)=>{
        var a=[]
        const keys =Object.keys(data)
        for(const s of keys){
            if(s.includes(k) && !Array.isArray(data[s])){
                a=[...a,data[s]]
            } 
        }
        return a;
    }
        return a;
    }
    const getSummary=(k)=>{
        var a=[]
        const keys =Object.keys(data)
        for(const s of keys){
            if(s.includes(k) && !Array.isArray(data[s])){
                a=[...a,data[s]]
            } 
        }
        return a;
    }
    return (
        <>
            {getSemesters(data).map(p=>

            <>
            <h4>{p[0]}</h4>
            <table className={`r-table table ${classnames({ 'table-divided': true })}`}>
                {<thead>
                    <tr>
                   { headers.map(l=><th className={'sorted-asc'}>
                        {l}
                        </th>)}
                    </tr>
                </thead>}
                <tbody>
                   {p[1].map(r=><tr>
                        <td>
                            {r.course.courseCode}
                        </td>
                        <td>
                        {r.course.courseUnit}
                        </td>
                        <td>
                            {r.total}
                        </td>
                        <td>
                            {r.grade}
                        </td>
                    </tr>)}
                    
                </tbody>
            </table>
            <span className='m-4 '>Semester Units:  {(getSummary(p[0])[0])}</span>
            <span className='m-4'>Semester Grade Points:  {(getSummary(p[0])[1])}</span>
            <span className='m-4'>Semetser GPA:  {(getSummary(p[0])[2])}</span><br/><h1></h1>
            </>)}
        </>
    )

}
export default ResultTable;