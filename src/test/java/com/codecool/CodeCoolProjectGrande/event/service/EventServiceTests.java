package com.codecool.CodeCoolProjectGrande.event.service;


import com.codecool.CodeCoolProjectGrande.event.Event;
import com.codecool.CodeCoolProjectGrande.event.EventStatus;
import com.codecool.CodeCoolProjectGrande.event.EventType;
import com.codecool.CodeCoolProjectGrande.event.controller.EventController;
import com.codecool.CodeCoolProjectGrande.event.event_provider.wroclaw_model.Address;
import com.codecool.CodeCoolProjectGrande.event.event_provider.wroclaw_model.Location;
import com.codecool.CodeCoolProjectGrande.event.event_provider.wroclaw_model.Offer;
import com.codecool.CodeCoolProjectGrande.event.event_provider.wroclaw_model.WroclawEvent;
import com.codecool.CodeCoolProjectGrande.event.repository.EventRepository;
import com.codecool.CodeCoolProjectGrande.user.User;
import com.codecool.CodeCoolProjectGrande.user.UserType;
import com.codecool.CodeCoolProjectGrande.user.password_reset.ResetPasswordToken;
import com.codecool.CodeCoolProjectGrande.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@SpringBootTest
class EventServiceTests {


    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private EventController eventController;

    @MockBean
    private EventRepository eventRepository;


    @MockBean
    UserRepository userRepository;



    private final Event event =
            Event.builder()
                    .eventId(UUID.randomUUID())
                    .name("Junit test")
                    .description("Junit test")
                    .startDate(LocalDateTime.of(2022, 12, 12, 20, 0))
                    .endDate(LocalDateTime.of(2022, 12, 14, 20, 0))
                    .eventType(EventType.CONCERT)
                    .linkToEventPage("Junit test.com")
                    .price(10)
                    .publicEvent(true)
                    .assignedUsers(new HashSet<>())
                    .location("test")
                    .latitude(222.0)
                    .longitude(222.0)
                    .eventStatus(EventStatus.PROMOTED)
                    .build();


    private final User user = User.builder().userId(UUID.randomUUID())
            .name("Test")
            .age(22)
            .email("email@gmail.com")
            .password("test1")
            .userType(UserType.USER)
            .location("Warsaw")
            .resetPasswordToken(new ResetPasswordToken())
            .build();


    @Test
    void createEventSuccessTest() {
        when(eventRepository.save(event)).thenReturn(event);
        Assertions.assertEquals(eventService.createEvent(event), Optional.of(event));
    }

    @Test
    void createEventFailure() {
        when(eventRepository.findEventByName(event.getName())).thenReturn(Optional.of(event));
        Assertions.assertEquals(eventService.createEvent(event), Optional.empty());
    }


    @Test
    void removeEventSuccessTest() {
        when(eventRepository.findEventByEventId(event.getEventId())).thenReturn(Optional.of(event));
        Assertions.assertEquals(eventService.removeEvent(event), Optional.of(event));
    }

    @Test
    void removeEventFailureTest() {
        when(eventRepository.removeEventByEventId(event.getEventId())).thenReturn(Optional.empty());
        Assertions.assertEquals(eventService.removeEvent(event), Optional.empty());
    }


    @Test
    void getAllEventsTest() {
        when(eventRepository.findAll()).thenReturn(Stream.of(event, event).collect(Collectors.toList()));
        Assertions.assertEquals(eventService.getEvents().size(), 2);
    }

    @Test
    void getEventByIdTest() {
        when(eventRepository.findEventByEventId(event.getEventId())).thenReturn(Optional.of(event));
        Assertions.assertEquals(eventService.getEventByID(event.getEventId()), Optional.of(event));
    }

    @Test
    void getAllEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        events.add(new Event());
        when(eventRepository.saveAll(events)).thenReturn(events);
        Assertions.assertEquals(eventService.saveAll(events), events);

    }

    @Test
    void findEventsByEventTypeTest() {
        List<Event> events = new ArrayList<>();
        events.add(event);
        events.add(event);
        when(eventRepository.findEventsByEventType(event.getEventType(), PageRequest.of(1,2))).thenReturn(events);
        Assertions.assertEquals(eventService.findEventsByEventType(event.getEventType(),1,2), events);
    }

    @Test
    void sortEventsAscTest() {
        List<Event> events = new ArrayList<>();
        events.add(event);
        events.add(event);
        when(eventRepository.findAllByNameContainingOrDescriptionContaining(event.getDescription(),
                event.getName(), PageRequest.of(1,2), Sort.by("name").ascending())).
                thenReturn(events);
        Assertions.assertEquals(eventService.sortEvents("name", true, event.getName(),1 ,2), events);
    }

    @Test
    void sortEventsDescTest() {
        List<Event> events = new ArrayList<>();
        events.add(event);
        events.add(event);
        when(eventRepository.findAllByNameContainingOrDescriptionContaining(event.getDescription(),
                event.getName(), PageRequest.of(1,2), Sort.by("name").descending())).
                thenReturn(events);
        Assertions.assertEquals(eventService.sortEvents("name", false, event.getName(),1 ,2), events);
    }

    @Test
    void assignUserToEventSuccessfullyTest() {
        event.assignUser(user);
        Map<Object, Object> data = new HashMap<>();
        data.put("eventId", event.getEventId());
        data.put("userEmail", user.getEmail());
        when(eventRepository.findEventByEventId(event.getEventId())).thenReturn(Optional.of(event));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Assertions.assertEquals(eventService.assignUserToEvent(data), Optional.of(event));
    }

    @Test
    void assignUserToEventFailureUserNotExistTest() {
        event.assignUser(user);
        String mockFailEmail = "test@test.pl";
        Map<Object, Object> data = new HashMap<>();
        data.put("eventId", event.getEventId());
        data.put("userEmail", user.getEmail());
        when(eventRepository.findEventByEventId(event.getEventId())).thenReturn(Optional.of(event));
        when(userRepository.findUserByEmail(mockFailEmail)).thenReturn(Optional.empty());
        Assertions.assertEquals(eventService.assignUserToEvent(data), Optional.empty());
    }

    @Test
    void assignUserToEventFailureEventNotExistTest() {
        event.assignUser(user);
        UUID mockEventId = UUID.randomUUID();
        Map<Object, Object> data = new HashMap<>();
        data.put("eventId", event.getEventId());
        data.put("userEmail", user.getEmail());
        when(eventRepository.findEventByEventId(mockEventId)).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Assertions.assertEquals(eventService.assignUserToEvent(data), Optional.empty());
    }


    @Test
    void editEventDescriptionByEventIdSuccessfullyTest() {
        Map<Object, Object> data = new HashMap<>();
        data.put("eventId", event.getEventId());
        data.put("userEmail", user.getEmail());
        when(eventRepository.findEventByEventId(event.getEventId())).thenReturn(Optional.of(event));
        Assertions.assertEquals(eventService.editEventDescriptionByEventId(data), Optional.of(event));


    }


    @Test
    void editEventDescriptionByEventIdFailureTest() {
        UUID mockNotExistEventId = UUID.randomUUID();
        Map<Object, Object> data = new HashMap<>();
        data.put("eventId", event.getEventId());
        data.put("userEmail", user.getEmail());
        when(eventRepository.findEventByEventId(mockNotExistEventId)).thenReturn(Optional.empty());
        Assertions.assertEquals(eventService.editEventDescriptionByEventId(data), Optional.empty());


    }



    //TODO refactor and right tests
    @Test
    void saveGlobalDataSuccessfullyTest(){
        List<String> artists = Stream.of("test", "test").toList();
        when(eventService.saveGlobalData()).thenReturn(artists);
        Assertions.assertEquals(eventController.saveGlobalData(), new ResponseEntity<>(HttpStatus.CREATED));
    }


//    @Test
//    void saveWroclawDataSuccessfullyTest(){
//        when(eventRepository.findEventByName(event.getName())).thenReturn(Optional.of(event));
//        when(eventService.createEvent(event)).thenReturn(Optional.of(event));
//        Assertions.assertEquals(eventController.saveWroclawData(), new ResponseEntity<>(HttpStatus.CREATED));
//    }



    @Test
    void getAssignedEventsSuccessfully() {
        List<Event> events = new ArrayList<>();
        Set<User> users = new HashSet<>();
        users.add(user);
        events.add(event);
        events.add(new Event());
        event.setAssignedUsers(users);
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findAllByAssignedUsersIn(users)).thenReturn(events);
        Assertions.assertEquals(eventService.getAssignedEvents(user.getEmail()), events);
    }

//    @Test
//    void serializeWroclawDataTest(){
//        WroclawEvent wroclawEvent = new WroclawEvent();
//        Offer offer = new Offer();
//        offer.setTitle("test");
//        Address address = new Address();
//        address.setCity("test");
//        address.setStreet("test");
//        Location location = new Location();
//        location.setLattiude(222.0);
//        location.setLongitude(222.0);
//        wroclawEvent.setAddress(address);
//        wroclawEvent.setOffer(offer);
//        wroclawEvent.setLocation(location);
//        wroclawEvent.setStartDate("2022-07-28T10:15:30");
//        wroclawEvent.setEndDate("2022-07-28T10:15:30");
//        Assertions.assertEquals(eventService.serializeWroclawData(wroclawEvent).getEventType(), EventType.OTHER);
//        Assertions.assertEquals(eventService.serializeWroclawData(wroclawEvent));
//    }

}
