package ua.myService.taxi.service;

import jakarta.jws.WebService;
import jakarta.xml.bind.*;
import org.xml.sax.SAXException;
import ua.myService.taxi.model.driver.Driver;
import ua.myService.taxi.model.order.Order;
import ua.myService.taxi.model.order.Orders;
import ua.myService.taxi.model.user.User;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebService(endpointInterface = "ua.myService.taxi.service.ServiceDAO", serviceName = "TaxiService", name = "Taxi", targetNamespace = LINK.ENTITY)
public final class ServiceDAOImpl implements ServiceDAO {
    private static ServiceDAO serviceDAO;
    private Logger log;

    private ServiceDAOImpl() {
        log = Logger.getLogger(ServiceDAOImpl.class.getName());
    }

    public static ServiceDAO getInstance() {
        if (serviceDAO == null) {
            serviceDAO = new ServiceDAOImpl();
        }
        return serviceDAO;
    }

    private void isValid(String xsd, String xml) throws ExceptionMessage {
        try {
            Validator validator = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(xsd)).newValidator();
            Source source = new StreamSource(xml);
            validator.validate(source);
        } catch (IOException | SAXException ex) {
            new File(xml).delete();
            throw new ExceptionMessage("Валідацію не пройдено");
        }
    }

    private Orders unMarshalOrders() {
        File file = new File(LINK.ORDERS_FILE);
        if (file.canRead()) {
            try {
                Unmarshaller um = JAXBContext.newInstance(ua.myService.taxi.model.order.ObjectFactory.class).createUnmarshaller();
                return (Orders) um.unmarshal(file);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return new Orders();
    }

    @Override
    public Order createOrder(Order order) throws ExceptionMessage {
        Orders orders = unMarshalOrders();
        orders.getOrder().add(order);
        try {
            Marshaller marshaller = JAXBContext.newInstance(ua.myService.taxi.model.order.ObjectFactory.class).createMarshaller();
            JAXBElement<Orders> jorder = new JAXBElement<>(
                    new QName(LINK.ORDER, "orders"),
                    Orders.class, orders);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(jorder, new File("xml/test.xml"));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        isValid("xml/completeOrders.xsd", "xml/test.xml");
        new File("xml/test.xml").delete();

        try {
            Marshaller marshaller = JAXBContext.newInstance(ua.myService.taxi.model.order.ObjectFactory.class).createMarshaller();
            JAXBElement<Orders> jorder = new JAXBElement<>(
                    new QName(LINK.ORDER, "orders"),
                    Orders.class, orders);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(jorder, new File(LINK.ORDERS_FILE));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        log.info("Було додано нове замовлення, перевірити можна у файлі orders.xml\n");
        return orders.getOrder().get(orders.getOrder().size() - 1);
    }


    @Override
    public Orders getHistory(User user) {
        Orders orders = unMarshalOrders();
        Orders ordersToReturn = new Orders();
        orders.getOrder().forEach(o -> {
            if (o.getUser().getId() == user.getId()) ordersToReturn.getOrder().add(o);
        });
        log.info("Було надано історію користувача з ID: " + user.getId()+"\n");
        return ordersToReturn;
    }

    @Override
    public List<Driver> getDriversBySurname(String surname) {
        log.info("Пошук водія за прізвищем: " + surname+"\n");
        return unMarshalOrders().getOrder().stream().map(Order::getDriver).filter(driver -> driver.getSurname().equals(surname)).toList();
    }

    @Override
    public List<Driver> getDriverByCode(String code) {
        log.info("Пошук водія за кодом: " + code+"\n");
        return unMarshalOrders().getOrder().stream().map(Order::getDriver).filter(driver -> driver.getCode().equals(code)).toList();
    }

    @Override
    public Orders getHistoryAboveMark(int mark) {
        Orders toReturn = new Orders();
        unMarshalOrders().getOrder().stream().filter(o -> o.getMark() > mark).forEach(order -> toReturn.getOrder().add(order));
        log.info("Отриманя історії з оцінкою вище за " + mark+"\n");
        return toReturn;
    }

    @Override
    public Orders getHistoryBelowMark(int mark) {
        Orders toReturn = new Orders();
        unMarshalOrders().getOrder().stream().filter(o -> o.getMark() < mark).forEach(order -> toReturn.getOrder().add(order));
        log.info("Отриманя історії з оцінкою нижче за " + mark+"\n");
        return toReturn;
    }
}
