package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.crud.v1.dto.AddressV1;
import by.aurorasoft.testapp.crud.v1.dto.PersonV1;
import by.aurorasoft.testapp.crud.v1.service.AddressV1Service;
import by.aurorasoft.testapp.crud.v1.service.PersonV1Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public final class ReplicationV1IT extends ReplicationIT<AddressV1, PersonV1> {

    @Autowired
    private AddressV1Service addressService;

    @Autowired
    private PersonV1Service personService;

    @Override
    protected AddressV1 createAddress(final Long id, final String country, final String city) {
        return new AddressV1(id, country, city);
    }

    @Override
    protected PersonV1 createPerson(final Long id,
                                    final String name,
                                    final String surname,
                                    final String patronymic,
                                    final LocalDate birthDate,
                                    final AddressV1 address) {
        return new PersonV1(id, name, surname, patronymic, birthDate, address);
    }

    @Override
    protected AddressV1 save(final AddressV1 address) {
        return addressService.save(address);
    }

    @Override
    protected PersonV1 save(final PersonV1 person) {
        return personService.save(person);
    }

    @Override
    protected List<AddressV1> saveAddresses(final List<AddressV1> addresses) {
        return addressService.saveAll(addresses);
    }

    @Override
    protected List<PersonV1> savePersons(final List<PersonV1> persons) {
        return personService.saveAll(persons);
    }

    @Override
    protected AddressV1 update(final AddressV1 address) {
        return addressService.update(address);
    }

    @Override
    protected PersonV1 update(final PersonV1 person) {
        return personService.update(person);
    }

    @Override
    protected AddressV1 updateAddressPartial(final Long id, final Object partial) {
        return addressService.updatePartial(id, partial);
    }

    @Override
    protected PersonV1 updatePersonPartial(final Long id, final Object partial) {
        return personService.updatePartial(id, partial);
    }

    @Override
    protected void deleteAddress(final Long id) {
        addressService.deleteById(id);
    }

    @Override
    protected void deletePerson(final Long id) {
        personService.deleteById(id);
    }

    @Override
    protected boolean isAddressExist(final Long id) {
        return addressService.existById(id);
    }
}
