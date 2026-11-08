package ua.com.foxminded.task.homework.rauf.mutants;

// HW2 - 10 manual mutants of GroupServiceImpl.create

import org.springframework.dao.DataIntegrityViolationException;

import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

public final class GroupCreateMutants {

    private GroupCreateMutants() {
    }

    // M1: flipped the id check (!= 0 to == 0)
    public static GroupDto mutant1(GroupRepository repo, GroupDto dto) {
        if (dto.getId() == 0) {
            throw new EntityAlreadyExistsException("M1");
        }
        Group group = buildEntity(dto);
        group = repo.saveAndFlush(group);
        return ConverterToDtoService.convert(group);
    }

    // M2: negated the year value
    public static GroupDto mutant2(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("M2");
        }
        Group group = new Group();
        group.setTitle(dto.getTitle());
        group.setYearEntry(-dto.getYearEntry());
        group = repo.saveAndFlush(group);
        return ConverterToDtoService.convert(group);
    }

    // M3: return null instead of the dto
    public static GroupDto mutant3(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("M3");
        }
        Group group = buildEntity(dto);
        group = repo.saveAndFlush(group);
        return null;
    }

    // M4: wrong exception thrown in catch
    public static GroupDto mutant4(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("M4");
        }
        Group group = buildEntity(dto);
        try {
            group = repo.saveAndFlush(group);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException("M4");
        }
        return ConverterToDtoService.convert(group);
    }

    // M5: removed setTitle call
    public static GroupDto mutant5(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("M5");
        }
        Group group = new Group();
        group.setYearEntry(dto.getYearEntry());
        group = repo.saveAndFlush(group);
        return ConverterToDtoService.convert(group);
    }

    // M6: removed setYearEntry call
    public static GroupDto mutant6(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("M6");
        }
        Group group = new Group();
        group.setTitle(dto.getTitle());
        group = repo.saveAndFlush(group);
        return ConverterToDtoService.convert(group);
    }

    // M7: used getOne instead of saveAndFlush
    public static GroupDto mutant7(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("M7");
        }
        Group group = buildEntity(dto);
        group = repo.getOne(group.getId());
        return ConverterToDtoService.convert(group);
    }

    // M8: return input dto instead of converted result
    public static GroupDto mutant8(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("M8");
        }
        Group group = buildEntity(dto);
        group = repo.saveAndFlush(group);
        return dto;
    }

    // M9: changed exception message (equivalent mutant)
    public static GroupDto mutant9(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("");
        }
        Group group = buildEntity(dto);
        group = repo.saveAndFlush(group);
        return ConverterToDtoService.convert(group);
    }

    // M10: discard save result
    public static GroupDto mutant10(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("M10");
        }
        Group group = buildEntity(dto);
        repo.saveAndFlush(group);
        return ConverterToDtoService.convert(group);
    }

    // original method (used as baseline)
    public static GroupDto original(GroupRepository repo, GroupDto dto) {
        if (dto.getId() != 0) {
            throw new EntityAlreadyExistsException("original");
        }
        Group group = buildEntity(dto);
        try {
            group = repo.saveAndFlush(group);
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotValidException("original", e);
        }
        return ConverterToDtoService.convert(group);
    }

    private static Group buildEntity(GroupDto dto) {
        Group group = new Group();
        group.setTitle(dto.getTitle());
        group.setYearEntry(dto.getYearEntry());
        return group;
    }

}
