//package FarmaciaERP.application.usecases.cliente;
//import FarmaciaERP.domain.entities.Customer;
//import FarmaciaERP.domain.repositories.IClienteRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EliminarClienteUseCase {
//
//    private final IClienteRepository clienteRepository;
//
//    public EliminarClienteUseCase(IClienteRepository clienteRepository) {
//        this.clienteRepository = clienteRepository;
//    }
//
//    public void ejecutar(Long distritoId) {
//        Customer customer = clienteRepository.findById(distritoId)
//                .orElseThrow(() -> new RuntimeException
//                        ("Customer con el userId:" + distritoId + "no existe."));
//        clienteRepository.deleteById(distritoId);
//    }
//}
