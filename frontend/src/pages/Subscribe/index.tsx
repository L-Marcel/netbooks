import styles from "./index.module.scss";
import useUser from "../../stores/useUser";
import { useState } from "react";
import Input from "@components/Input";
import "@components/Input/index.module.scss";
import Button from "@components/Button";

export default function Subscribe(){
    //const login = useUser();
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirm, setConfirm] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e : React.FormEvent) => {
        e.preventDefault();
        
        if (!email || !password) {
            alert("preencha os campos");
            // Alert para preenchimento de campos
            return;
        }
        if (password != confirm) {
            alert("senhas diferentes");
            // Alert para informar que a senha e a confirmação estão diferentes
            return
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
                    <h1>Criar Conta</h1>
                    <p>Cadastre-se para acessar todos os recursos</p>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className={styles.form_group}>
                        <label htmlFor="name">Nome</label>
                        <Input
                            type="text"
                            id="name"
                            value={name}
                            setFunction={setName}
                            placeholder="Seu nome"
                        />
                    </div>

                    <div className={styles.form_group}>
                        <label htmlFor="email">Email</label>
                        <Input
                            type="text"
                            id="email"
                            value={email}
                            setFunction={setEmail}
                            placeholder="seu@email.com"
                        />
                    </div>
                    
                    <div className={styles.form_group}>
                        <label htmlFor="password">Senha</label>
                        <Input
                            type="password"
                            id="password"
                            value={password}
                            setFunction={setPassword}
                            placeholder="*****"
                        />
                    </div>
                    
                    <div className={styles.form_group}>
                        <label htmlFor="confirm">Confirmar senha</label>
                        <Input
                            type="password"
                            id="confirm"
                            value={confirm}
                            setFunction={setConfirm}
                            placeholder="*****"
                        />
                    </div>
                    
                    <div className={styles.form_group}>
                        <Button
                            type="submit" 
                            theme="dark"
                            text={isLoading ? "Criando..." : "Criar conta"}
                            disabled={isLoading}
                        />
                    </div>
                    
                </form>
                <div className={styles.hasLogin}>
                    <p>
                        Já tem uma conta? <a href="#login" className={styles.login}>Entrar</a>
                    </p>
                </div>
            </div>
        </div>
            
    );
}