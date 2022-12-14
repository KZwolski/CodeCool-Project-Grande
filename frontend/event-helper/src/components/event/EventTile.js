import moment from "moment";
import "../../css/eventTile.css";
import {useState,useEffect} from "react";
export default function EventTile(props) {

    const [name, setName] = useState(props.name);

    const [desc, setDesc] = useState(props.description);

    const [location, setLocation]= useState(props.location)

    const regex = /(<([^>]+)>)/ig;
    const result = desc.replace(regex, '');

    useEffect(() => {
        formatEventProps().then(r => console.log(r))

    },[] );

    const formatEventProps = async () =>{
        if(props.name.toString().length >= 45){
            setName(props.name.toString().substring(0,42)+"...");
        }
        if(props.description.toString().length >= 300){
            setDesc(props.description.toString().substring(0,297)+"...");
        }
        if(props.description.toString().length >= 30){
            setLocation(props.location.toString().substring(0,27)+"...");
        }
    }

    return (
        <a href={'/event/' + props.eventId} >
            <div className="card">
                <div className="card-header">
                    <img src={props.logo} alt="rover"/>
                </div>
                <div className="card-body">
                    <span className={`tag ${props.eventType}`} id="event-type">{props.eventType}</span>
                    <h4 id="event-names">
                        {name}
                    </h4>
                    <p id="event-desc">
                        {result}
                    </p>
                    <div className="user">
                        <div className="user-info">
                            <h5 id="event-location">{location}</h5>
                            <small id="event-date">{moment.utc(props.startDate).local().startOf('seconds').fromNow()}</small>
                        </div>
                    </div>
                </div>
            </div></a>

    )


}