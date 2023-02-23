import React,{useEffect,useState} from "react";
import EventTile from "./EventTile";

export const OrganizedByUser = (props) => {

    const [events, setEvents] = useState([]);

    useEffect(() => {
        getEvents().then(r => console.log(r))

    }, [props.userId]);

    const getEvents = async () =>{
        const response = await fetch(`${process.env.REACT_APP_URL}/api/events/assign-to-user/${props.userId}&0&5`);
        const data = await response.json();
        setEvents(data);
    }

    return (
        <>  {events.map((event) => (
            <EventTile name={event.name}
                       location={event.location}
                       eventId={event.eventId}
                       logo={event.logo}
                       startDate={event.startDate}
            />
        ))}
        </>
    )
}