import React,{useEffect, useState} from 'react';
import {CopyToClipboard} from "react-copy-to-clipboard";
import {toast} from 'react-toastify';
import {useParams} from 'react-router-dom';
import moment from "moment";
import './App.css';

function Home() {
  const [url, setUrl] = useState<String>("");
  const [ip,setIp] = useState<String>("");
  const [allUrl,setAllUrl] = useState<any>([]);
  const match = { params: useParams() };
  useEffect(() => {
    if(Object.keys(match?.params).length>0){
        (async function () {
            const response = await fetch(
            `http://localhost:8080/api/checkShortUrl?shortUrlId=${match?.params?.id}`
          ,{
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
           
          });
          const data = await response.json();
          if(data){
            if(data?.data[0]?.expiry){
                toast.error(data?.message,{delay:2000})
                window.open(window.location.origin,'_self');
            }
            else{
                window.open(data?.data[0]?.originalUrl,'_self');
            }
          }
        })();

    }
  getIp();
  if(ip){
  fetchWithIp();
  }
  else{
  fetchAll();
  }
  },[])
  const shortenUrl = async (e:any) => {
    e.preventDefault();
    try {
      const url_obj ={
        "ip":ip,
        "originalUrl":url,
        "shortUrl":window.location.origin+'/'
      }
      const response = await fetch(
        `http://localhost:8080/api/createUrl`
      ,{
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(url_obj),
      });
      const data = await response.json()
      if(data){
        ip ? fetchWithIp() : fetchAll();
        toast.success("Url Generate Success");
      }
    } catch (e) {
      toast.error(`${e}`,{delay:2000});
    }
  };
  const fetchAll  = async () => {
    const response = await fetch(
       `http://localhost:8080/api/fetchAllUrl`
      ,{
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
       
      });
      const data = await response.json()
      if(data){
        setAllUrl(data?.data)
      }else{
        setAllUrl([])
      }

  }
  const fetchWithIp  = async () => {
    const response = await fetch(
        `http://localhost:8080/api/fetchAllUrl?ip=${ip}`
       ,{
         method: "GET",
         headers: {
           "Content-Type": "application/json",
         },
        
       });
       const data = await response.json()
       if(data){
         setAllUrl(data?.data)
       }else{
         setAllUrl([])
       }
  }
  const getIp = async () => {
    try {
    
        const response = await fetch(
          `https://api.ipify.org/?format=json`
        )
        const data = await response.json()
        if(data?.ip){
            setIp(data.ip);
        }
        else{
            setIp(ip);
        }
        
      } catch (e) {
        toast.error(`${e}`,{delay:2000});
      }
  }
  const handleChange = (e:any) => {
    if(e.target.value.includes("http://localhost")){
     toast.error("Please enter valid url",{delay:2000});
    }
    setUrl(e.target.value)
  }
  const deleteAll = async () => {
    const response = await fetch(
        `http://localhost:8080/api/deleteAll`
       ,{
         method: "DELETE",
         headers: {
           "Content-Type": "application/json",
         },
        
       });
       const data = await response.json()
       ip ? fetchWithIp() : fetchAll();
        
  }
  const deleteOne =  async (id:any) => {
    const response = await fetch(
        `http://localhost:8080/api/deleteUrl/${id}`
       ,{
         method: "DELETE",
         headers: {
           "Content-Type": "application/json",
         },
        
       });
       const data = await response.json()
      ip ? fetchWithIp() : fetchAll();
  }
  return (
    <div className="app">
     <div className='shortener'>
        <h2>URL shortener</h2>
        <form onSubmit={shortenUrl}>
          <input
            placeholder="Enter URL"
            //@ts-ignore
            value={url}
            onChange={(e:any) => {handleChange(e)}}/>
          <button>Submit</button>
        </form>
        {/* Section to view shortened URLS */}
        <button style={{marginTop:"20px",textAlign:"end"}} className='btn-danger' onClick={() => deleteAll()}>Clear All</button>
        <table>
	<thead>
	<tr className="table-headers">
    <th>Short URL</th>
    <th>Created At</th>
    <th>Copy</th>
    <th>Action</th>
    
	</tr>
	</thead>
	<tbody>
   {allUrl && allUrl.length>0 && allUrl.map((item:any,index:number) =>{
    return <>
    <tr>
      <td>
      <div className="shortener__viewShot">
          <a href={item?.shortUrl}>{item?.shortUrl}</a> 
        </div>
      </td>
      <th className="mobile-header">
           {moment(item?.createdDate).fromNow()}
      </th><td>{moment(item?.createdDate).fromNow()}</td>
      <th className="mobile-header">
     <a href={item?.shortUrl}>{item?.shortUrl}</a>
      </th><td>
      <CopyToClipboard 
        key={item}
          //@ts-ignore
          text={item?.shortUrl}>
          
            <button className='btn' onClick={() => toast.success("The URL has been copied",{delay:2000})}>copy</button>
      </CopyToClipboard>  
      </td>
      <th className="mobile-header"></th><td>
        <button type='button' className='btn-danger' onClick={() => deleteOne(item?.id)}>Delete</button>
      </td>
      
    </tr>
    </>
   })}

	</tbody>
</table>
      </div>
    </div>
  );
}

export default Home;
