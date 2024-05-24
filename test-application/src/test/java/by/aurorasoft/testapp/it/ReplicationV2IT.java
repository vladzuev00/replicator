package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.crud.v2.dto.Address;
import by.aurorasoft.testapp.crud.v2.dto.Person;
import by.aurorasoft.testapp.crud.v2.service.AddressV2Service;
import by.aurorasoft.testapp.crud.v2.service.PersonV2Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public final class ReplicationV2IT extends ReplicationIT<Address, Person> {

    @Autowired
    private AddressV2Service addressService;

    @Autowired
    private PersonV2Service personService;

    @Override
    protected Address createAddress(final Long id, final String country, final String city) {
        return new Address(id, country, city);
    }

    @Override
    protected Person createPerson(final Long id,
                                  final String name,
                                  final String surname,
                                  final String patronymic,
                                  final LocalDate birthDate,
                                  final Address address) {
        return new Person(id, name, surname, patronymic, birthDate, address);
    }

    @Override
    protected Address save(final Address address) {
        return addressService.save(address);
    }

    @Override
    protected Person save(final Person person) {
        return personService.save(person);
    }

    @Override
    protected List<Address> saveAddresses(final List<Address> addresses) {
        return addressService.saveAll(addresses);
    }

    @Override
    protected List<Person> savePersons(final List<Person> persons) {
        return personService.saveAll(persons);
    }

    @Override
    protected Address update(final Address address) {
        return addressService.update(address);
    }

    @Override
    protected Person update(final Person person) {
        return personService.update(person);
    }

}
