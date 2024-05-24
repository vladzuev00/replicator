package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.crud.v1.dto.Address;
import by.aurorasoft.testapp.crud.v1.dto.Person;
import by.aurorasoft.testapp.crud.v1.service.AddressV1Service;
import by.aurorasoft.testapp.crud.v1.service.PersonV1Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public final class ReplicationV1IT extends ReplicationIT<Address, Person> {

    @Autowired
    private AddressV1Service addressService;

    @Autowired
    private PersonV1Service personService;

    @Override
    protected Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    @Override
    protected Address createAddress(final String country, final String city) {
        return Address.builder()
                .country(country)
                .city(city)
                .build();
    }

    @Override
    protected Address createAddress(final Long id, final String country, final String city) {
        return new Address(id, country, city);
    }

    @Override
    protected Person createPerson(final String name,
                                  final String surname,
                                  final String patronymic,
                                  final LocalDate birthDate,
                                  final Address address) {
        return Person.builder()
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .birthDate(birthDate)
                .address(address)
                .build();
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
}
