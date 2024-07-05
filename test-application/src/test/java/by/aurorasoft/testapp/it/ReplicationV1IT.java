package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.crud.v1.dto.AddressV1;
import by.aurorasoft.testapp.crud.v1.dto.PersonV1;
import by.aurorasoft.testapp.crud.v1.service.AddressV1Service;
import by.aurorasoft.testapp.crud.v1.service.PersonV1Service;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.stream;
import static org.junit.Assert.assertTrue;

public final class ReplicationV1IT extends ReplicationIT<AddressV1, PersonV1> {

    @Autowired
    private AddressV1Service addressService;

    @Autowired
    private PersonV1Service personService;

    @Test
    public void deleteAllShouldBeReplicated() {
        Long firstGivenAddressId = 259L;
        Long secondGivenAddressId = 260L;
        Long thirdGivenAddressId = 261L;
        List<AddressV1> givenAddresses = List.of(
                createAddress(firstGivenAddressId),
                createAddress(secondGivenAddressId),
                createAddress(thirdGivenAddressId)
        );

        executeWaitingReplication(() -> addressService.deleteAll(givenAddresses), 1, 0, false);

        assertTrue(isAddressesNotExist(firstGivenAddressId, secondGivenAddressId, thirdGivenAddressId));
    }

    @Override
    protected AddressV1 createAddress(Long id, String country, String city) {
        return new AddressV1(id, country, city);
    }

    @Override
    protected PersonV1 createPerson(Long id,
                                    String name,
                                    String surname,
                                    String patronymic,
                                    LocalDate birthDate,
                                    AddressV1 address) {
        return new PersonV1(id, name, surname, patronymic, birthDate, address);
    }

    @Override
    protected AddressV1 save(AddressV1 address) {
        return addressService.save(address);
    }

    @Override
    protected PersonV1 save(PersonV1 person) {
        return personService.save(person);
    }

    @Override
    protected List<AddressV1> saveAddresses(List<AddressV1> addresses) {
        return addressService.saveAll(addresses);
    }

    @Override
    protected List<PersonV1> savePersons(List<PersonV1> persons) {
        return personService.saveAll(persons);
    }

    @Override
    protected AddressV1 update(AddressV1 address) {
        return addressService.update(address);
    }

    @Override
    protected PersonV1 update(PersonV1 person) {
        return personService.update(person);
    }

    @Override
    protected AddressV1 updateAddressPartial(Long id, Object partial) {
        return addressService.updatePartial(id, partial);
    }

    @Override
    protected PersonV1 updatePersonPartial(Long id, Object partial) {
        return personService.updatePartial(id, partial);
    }

    @Override
    protected void deleteAddress(Long id) {
        addressService.deleteById(id);
    }

    @Override
    protected void deletePerson(Long id) {
        personService.deleteById(id);
    }

    @Override
    protected boolean isAddressExist(Long id) {
        return addressService.existById(id);
    }

    private boolean isAddressesNotExist(Long... ids) {
        return stream(ids).allMatch(id -> !addressService.existById(id) && !replicatedAddressRepository.existsById(id));
    }
}
