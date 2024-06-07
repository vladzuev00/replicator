package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.crud.v2.dto.AddressV2;
import by.aurorasoft.testapp.crud.v2.dto.PersonV2;
import by.aurorasoft.testapp.crud.v2.service.AddressV2Service;
import by.aurorasoft.testapp.crud.v2.service.PersonV2Service;
import by.aurorasoft.testapp.model.AddressName;
import by.aurorasoft.testapp.model.PersonAddress;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public final class ReplicationV2IT extends ReplicationIT<AddressV2, PersonV2> {

    @Autowired
    private AddressV2Service addressService;

    @Autowired
    private PersonV2Service personService;

    @Override
    protected AddressV2 createAddress(final Long id, final String country, final String city) {
        return new AddressV2(id, country, city);
    }

    @Override
    protected PersonV2 createPerson(final Long id,
                                    final String name,
                                    final String surname,
                                    final String patronymic,
                                    final LocalDate birthDate,
                                    final AddressV2 address) {
        return new PersonV2(id, name, surname, patronymic, birthDate, address);
    }

    @Override
    protected AddressV2 save(final AddressV2 address) {
        return addressService.save(address);
    }

    @Override
    protected PersonV2 save(final PersonV2 person) {
        return personService.save(person);
    }

    @Override
    protected List<AddressV2> saveAddresses(final List<AddressV2> addresses) {
        return addressService.saveAll(addresses);
    }

    @Override
    protected List<PersonV2> savePersons(final List<PersonV2> persons) {
        return personService.saveAll(persons);
    }

    @Override
    protected AddressV2 update(final AddressV2 address) {
        return addressService.update(address);
    }

    @Override
    protected PersonV2 update(final PersonV2 person) {
        return personService.update(person);
    }

    @Override
    protected AddressV2 updateAddressPartial(final Long id, final AddressName name) {
        return addressService.updatePartial(id, name);
    }

    @Override
    protected PersonV2 updatePersonPartial(final Long id, final Object partial) {
        return personService.updatePartial(id, partial);
    }

    @Override
    protected void deleteAddress(final Long id) {
        addressService.delete(id);
    }

    @Override
    protected void deletePerson(final Long id) {
        personService.delete(id);
    }

    @Override
    protected boolean isAddressExist(final Long id) {
        return addressService.isExist(id);
    }
}
