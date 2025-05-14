import styles from "./index.module.scss";
import useUser from "../../stores/useUser";
import { useState } from "react";

export default function Login(){
    //const login = useUser();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e : React.FormEvent) => {
        e.preventDefault();
        
        if (!email || !password) {
            alert("preencha os campos");
            // Alert para preenchimento de campos
            return;
        }

        setIsLoading(true);
        try {
            alert("ok");
            // Alert de sucesso e redirecionamento de página
        } catch (error) {
            // Alert de erro
        }
        finally{
            setIsLoading(false);
        }
    }

    return(
        <div className={styles.container}>
            <div className={styles.box}>
                <div className={styles.text}>
                    <h1>Entrar</h1>
                    <p>Entre para acessar sua conta</p>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className={styles.form_group}>
                        <label htmlFor="email">Email</label>
                        <input 
                            type="email" 
                            name="email" 
                            id="email" 
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="seu@email.com"
                            required
                        />
                    </div>
                    
                    <div className={styles.form_group}>
                        <label htmlFor="password">Senha</label>
                        <input
                            type="password"
                            name="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="*****"
                            required
                        />
                    </div>
                    
                    <div className={styles.form_group}>
                        <button 
                            type="submit" 
                            className={styles.submit}
                            disabled={isLoading}
                        >
                            {isLoading ? "Entrando..." : "Entrar"}
                        </button>
                    </div>
                    
                </form>
                <div className={styles.hasSubscription}>
                    <p>
                        Não tem uma conta? <a href="#cadastro" className={styles.subscription}>Cadastre-se</a>
                    </p>
                </div>
            </div>
        </div>
            
    );
}