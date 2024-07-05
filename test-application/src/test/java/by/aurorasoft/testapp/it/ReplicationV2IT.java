package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.crud.v2.dto.AddressV2;
import by.aurorasoft.testapp.crud.v2.dto.PersonV2;
import by.aurorasoft.testapp.crud.v2.service.AddressV2Service;
import by.aurorasoft.testapp.crud.v2.service.PersonV2Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public final class ReplicationV2IT extends ReplicationIT<AddressV2, PersonV2> {

    @Autowired
    private AddressV2Service addressService;

    @Autowired
    private PersonV2Service personService;

    @Override
    protected AddressV2 createAddress(Long id, String country, String city) {
        return new AddressV2(id, country, city);
    }

    @Override
    protected PersonV2 createPerson(Long id,
                                    String name,
                                    String surname,
                                    String patronymic,
                                    LocalDate birthDate,
                                    AddressV2 address) {
        return new PersonV2(id, name, surname, patronymic, birthDate, address);
    }

    @Override
    protected AddressV2 save(AddressV2 address) {
        return addressService.save(address);
    }

    @Override
    protected PersonV2 save(PersonV2 person) {
        return personService.save(person);
    }

    @Override
    protected List<AddressV2> saveAddresses(List<AddressV2> addresses) {
        return addressService.saveAll(addresses);
    }

    @Override
    protected List<PersonV2> savePersons(List<PersonV2> persons) {
        return personService.saveAll(persons);
    }

    @Override
    protected AddressV2 update(AddressV2 address) {
        return addressService.update(address);
    }

    @Override
    protected PersonV2 update(PersonV2 person) {
        return personService.update(person);
    }

    @Override
    protected AddressV2 updateAddressPartial(Long id, Object partial) {
        return addressService.updatePartial(id, partial);
    }

    @Override
    protected PersonV2 updatePersonPartial(Long id, Object partial) {
        return personService.updatePartial(id, partial);
    }

    @Override
    protected void deleteAddress(Long id) {
        addressService.delete(id);
    }

    @Override
    protected void deletePerson(Long id) {
        personService.delete(id);
    }

    @Override
    protected boolean isAddressExist(Long id) {
        return addressService.isExist(id);
    }
}
