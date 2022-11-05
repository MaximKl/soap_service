package ua.myService.taxi.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;
import ua.myService.taxi.model.driver.Driver;
import ua.myService.taxi.model.order.Order;
import ua.myService.taxi.model.order.Orders;
import ua.myService.taxi.model.user.User;

import java.util.List;

@WebService(serviceName = "TaxiService", name = "Taxi", targetNamespace = LINK.ENTITY)
public sealed interface ServiceDAO permits ServiceDAOImpl {

    @WebMethod(operationName = "createOrder", action = "create")
    @RequestWrapper(className = "service.orderRequest", localName = "CreateReq", targetNamespace = LINK.ENTITY)
    @ResponseWrapper(className = "service.orderResponse", localName = "CrateResp", targetNamespace = LINK.ENTITY)
    Order createOrder(@WebParam(name = "orderToSend") Order order) throws ExceptionMessage;

    @WebMethod(operationName = "getUsersOrderHistory", action = "get")
    @RequestWrapper(className = "service.historyRequest", localName = "HistoryReq", targetNamespace = LINK.ENTITY)
    @ResponseWrapper(className = "service.historyResponse", localName = "HistoryResp", targetNamespace = LINK.ENTITY)
    Orders getHistory(@WebParam(name = "user") User user);

    @WebMethod(operationName = "getDriverBySurname", action = "get")
    @RequestWrapper(className = "service.driverBySurnameRequest", localName = "DriverBySurnameReq", targetNamespace = LINK.ENTITY)
    @ResponseWrapper(className = "service.driverBySurnameResponse", localName = "DriverBySurnameResp", targetNamespace = LINK.ENTITY)
    List<Driver> getDriversBySurname(@WebParam(name = "driversSurname") String surname);

    @WebMethod(operationName = "getDriverByCode", action = "get")
    @RequestWrapper(className = "getDriverByCodeRequest", localName = "DriverByCodeReq", targetNamespace = LINK.ENTITY)
    @ResponseWrapper(className = "getDriverByCodeResponse", localName = "DriverByCodeResp", targetNamespace = LINK.ENTITY)
    List<Driver> getDriverByCode(@WebParam(name = "driversCode") String code);

    @WebMethod(operationName = "getHistoryAboveMark", action = "get")
    @RequestWrapper(className = "getHistoryAboveMarkRequest", localName = "HistoryAboveMarkReq", targetNamespace = LINK.ENTITY)
    @ResponseWrapper(className = "getHistoryAboveMarkResponse", localName = "HistoryAboveMarkResp", targetNamespace = LINK.ENTITY)
    Orders getHistoryAboveMark(@WebParam(name = "driversMark") int mark);

    @WebMethod(operationName = "getHistoryBelowMark", action = "get")
    @RequestWrapper(className = "getHistoryBelowMarkRequest", localName = "HistoryBelowMarkReq", targetNamespace = LINK.ENTITY)
    @ResponseWrapper(className = "getHistoryBelowMarkResponse", localName = "HistoryBelowMarkResp", targetNamespace = LINK.ENTITY)
    Orders getHistoryBelowMark(@WebParam(name = "driversMark") int mark);

}
