import { Outlet } from 'react-router-dom';
import { Header } from '../components/Header';

export function DefaultLayout() {
    return (
        // Toda a configuração de fundo de galáxia e fontes fica aqui na casca
        <div className="min-h-screen font-sans text-gray-200">

            {/* O Header fica fixo para sempre */}
            <Header />

            {/* Margens da tela principal para o mobile */}
            <main className="px-3 pt-24 pb-6 max-w-md mx-auto">

                {/* A MÁGICA ACONTECE AQUI: O React vai injetar as páginas dinamicamente dentro deste Outlet */}
                <Outlet />

            </main>
        </div>
    );
}