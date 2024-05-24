package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.crud.v2.dto.Address;
import by.aurorasoft.testapp.crud.v2.dto.Person;
import by.aurorasoft.testapp.crud.v2.service.AddressV2Service;
import by.aurorasoft.testapp.crud.v2.service.PersonV2Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public final class ReplicationV2IT extends ReplicationIT<Address, Person> {

    @Autowired
    private AddressV2Service addressService;

    @Autowired
    private PersonV2Service personService;

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

}
